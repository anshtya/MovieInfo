package com.anshtya.movieinfo.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.data.model.NetworkResponse
import com.anshtya.movieinfo.data.model.SearchItem
import com.anshtya.movieinfo.data.repository.SearchRepository
import com.anshtya.movieinfo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private var includeAdult: Boolean = false

    init {
        getIncludeAdult()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val searchSuggestions: StateFlow<List<SearchItem>> = _searchQuery
        .filter { it.isNotEmpty() }
        .mapLatest { query ->
            delay(200)
            val response = searchRepository.getSearchSuggestions(
                query = query,
                includeAdult = includeAdult
            )
            when (response) {
                is NetworkResponse.Success -> response.data

                is NetworkResponse.Error -> {
                    _errorMessage.update { response.errorMessage }
                    emptyList()
                }
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

    fun onBack() {
        _searchQuery.update { "" }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getIncludeAdult() {
        userRepository.userData
            .map { it.includeAdultResults }
            .distinctUntilChanged()
            .onEach { includeAdult = it }
            .launchIn(viewModelScope)
    }
}