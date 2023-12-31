package com.anshtya.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anshtya.core.model.SearchItem
import com.anshtya.core.model.SearchSuggestion
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.SearchRepository
import com.anshtya.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _includeAdult = userDataRepository.userData
        .map { it.includeAdultResults }
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            replay = 1
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchInput = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchResultTabs = SearchResultTab.entries.toList()
    val searchResultTabs = _searchResultTabs.map { it.displayName }

    private val _selectedResultTabIndex = MutableStateFlow(0)
    val selectedResultTabIndex = _selectedResultTabIndex.asStateFlow()

    private val _selectedResultTab = _selectedResultTabIndex
        .mapLatest { index -> _searchResultTabs[index] }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SearchResultTab.MOVIES
        )

    private val _showError = MutableStateFlow<Boolean?>(null)
    val showError = _showError.asStateFlow()

    val searchResults: Flow<PagingData<SearchItem>> =
        combine(_selectedResultTab, _searchInput, ::Pair)
            .flatMapLatest { (tab, inputQuery) ->
                if (inputQuery.isNotEmpty()) {
                    when (tab) {
                        SearchResultTab.MOVIES -> searchRepository.searchMovie(
                            query = inputQuery,
                            includeAdult = _includeAdult.first()
                        )

                        SearchResultTab.TV -> searchRepository.searchTV(
                            query = inputQuery,
                            includeAdult = _includeAdult.first()
                        )
                    }
                } else {
                    flow { PagingData.empty<SearchItem>() }
                }
            }
            .cachedIn(viewModelScope)

    val searchSuggestions: StateFlow<List<SearchSuggestion>> = _searchQuery
        .mapLatest { query ->
            if (query.isNotEmpty()) {
                val response = searchRepository.multiSearch(
                    query = query,
                    includeAdult = _includeAdult.first()
                )
                when (response) {
                    is NetworkResponse.Success -> response.data
                    is NetworkResponse.Error -> {
                        _showError.update { true }
                        emptyList()
                    }
                }
            } else {
                emptyList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )


    fun changeSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun onSearch() {
        _isSearching.update { true }
        _searchInput.update { _searchQuery.value }
        _selectedResultTabIndex.update { 0 }
    }

    fun changeSearchResultTab(index: Int) {
        _selectedResultTabIndex.update { index }
    }

    fun onBack() {
        _isSearching.update { false }
        _searchInput.update { "" }
        _searchQuery.update { "" }
    }

    fun onErrorShown() {
        _showError.update { null }
    }
}