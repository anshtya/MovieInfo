package com.anshtya.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anshtya.data.model.SearchItem
import com.anshtya.data.model.SearchSuggestion
import com.anshtya.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchInput = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _selectedResultTab = MutableStateFlow(SearchResultTab.MOVIES)
    val selectedResultTab = _selectedResultTab.asStateFlow()

    val searchResults: Flow<PagingData<SearchItem>> =
        combine(_selectedResultTab, _searchInput, ::Pair)
            .flatMapLatest { (tab, inputQuery) ->
                if (inputQuery.isNotEmpty()) {
                    when (tab) {
                        SearchResultTab.MOVIES -> searchRepository.searchMovie(inputQuery)
                        SearchResultTab.TV -> searchRepository.searchTV(inputQuery)
                    }
                } else {
                    flow { PagingData.empty<SearchItem>() }
                }
            }
            .cachedIn(viewModelScope)

    val searchSuggestions: StateFlow<List<SearchSuggestion>> = _searchQuery
        .mapLatest { query ->
            if (query.isNotEmpty()) {
                searchRepository.multiSearch(query).take(6)
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
        _selectedResultTab.update { SearchResultTab.MOVIES }
        _searchQuery.update { "" }
    }

    fun changeSearchResultTab(resultTab: SearchResultTab) {
        _selectedResultTab.update { resultTab }
    }

    fun onBack() {
        _isSearching.update { false }
        _searchInput.update { "" }
        _searchQuery.update { "" }
    }
}