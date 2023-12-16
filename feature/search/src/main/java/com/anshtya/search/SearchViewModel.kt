package com.anshtya.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.data.model.SearchSuggestion
import com.anshtya.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val searchQueryArgument = savedStateHandle.getStateFlow(
        key = "",
        initialValue = ""
    )

    val searchResults = searchQueryArgument
        .mapLatest {
            homeRepository.multiSearch(it)
        }

    val searchSuggestions: StateFlow<List<SearchSuggestion>> = _searchQuery
        .mapLatest { query ->
            if (query.isNotEmpty()) {
                homeRepository.multiSearch(query)
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
}