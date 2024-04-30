package com.anshtya.movieinfo.feature.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.model.content.MovieListCategory
import com.anshtya.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _nowPlayingMovies = MutableStateFlow(ContentUiState(MovieListCategory.NOW_PLAYING))
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow(ContentUiState(MovieListCategory.POPULAR))
    val popularMovies = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow(ContentUiState(MovieListCategory.TOP_RATED))
    val topRatedMovies = _topRatedMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow(ContentUiState(MovieListCategory.UPCOMING))
    val upcomingMovies = _upcomingMovies.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getNowPlayingMovies()
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }
    fun appendItems(category: MovieListCategory) {
        when (category) {
            MovieListCategory.NOW_PLAYING -> getNowPlayingMovies()
            MovieListCategory.POPULAR -> getPopularMovies()
            MovieListCategory.TOP_RATED -> getTopRatedMovies()
            MovieListCategory.UPCOMING -> getUpcomingMovies()
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getNowPlayingMovies() {
        if (_nowPlayingMovies.value.isLoading) return

        viewModelScope.launch {
            _nowPlayingMovies.update { it.copy(isLoading = true) }

            val stateValue = _nowPlayingMovies.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _nowPlayingMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getPopularMovies() {
        if (_popularMovies.value.isLoading) return

        viewModelScope.launch {
            _popularMovies.update { it.copy(isLoading = true) }

            val stateValue = _popularMovies.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _popularMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getTopRatedMovies() {
        if (_topRatedMovies.value.isLoading) return

        viewModelScope.launch {
            _topRatedMovies.update { it.copy(isLoading = true) }

            val stateValue = _topRatedMovies.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _topRatedMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getUpcomingMovies() {
        if (_upcomingMovies.value.isLoading) return

        viewModelScope.launch {
            _upcomingMovies.update { it.copy(isLoading = true) }

            val stateValue = _upcomingMovies.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _upcomingMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun handleResponse(
        response: NetworkResponse<List<ContentItem>>
    ): Pair<List<ContentItem>, Boolean> {
        return when (response) {
            is NetworkResponse.Success -> {
                val items = response.data
                Pair(items, items.isEmpty())
            }

            is NetworkResponse.Error -> {
                _errorMessage.update { response.errorMessage }
                Pair(emptyList(), false)
            }
        }
    }
}

data class ContentUiState(
    val items: List<ContentItem>,
    val isLoading: Boolean,
    val endReached: Boolean,
    val page: Int,
    val category: MovieListCategory
) {
    constructor(category: MovieListCategory) : this(
        items = emptyList(),
        isLoading = false,
        endReached = false,
        page = 0,
        category = category
    )
}