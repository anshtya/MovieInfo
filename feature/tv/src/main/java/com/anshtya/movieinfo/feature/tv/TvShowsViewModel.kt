package com.anshtya.movieinfo.feature.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.content.ContentItem
import com.anshtya.movieinfo.core.model.content.TvShowListCategory
import com.anshtya.movieinfo.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _airingTodayTvShows = MutableStateFlow(
        ContentUiState(TvShowListCategory.AIRING_TODAY)
    )
    val airingTodayTvShows = _airingTodayTvShows.asStateFlow()

    private val _onAirTvShows = MutableStateFlow(ContentUiState(TvShowListCategory.ON_THE_AIR))
    val onAirTvShows = _onAirTvShows.asStateFlow()

    private val _popularTvShows = MutableStateFlow(ContentUiState(TvShowListCategory.POPULAR))
    val popularTvShows = _popularTvShows.asStateFlow()

    private val _topRatedTvShows = MutableStateFlow(ContentUiState(TvShowListCategory.TOP_RATED))
    val topRatedTvShows = _topRatedTvShows.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getAiringTodayTvShows()
        getOnAirTvShows()
        getPopularTvShows()
        getTopRatedTvShows()
    }

    fun appendItems(category: TvShowListCategory) {
        when (category) {
            TvShowListCategory.AIRING_TODAY -> getAiringTodayTvShows()
            TvShowListCategory.ON_THE_AIR -> getOnAirTvShows()
            TvShowListCategory.POPULAR -> getPopularTvShows()
            TvShowListCategory.TOP_RATED -> getTopRatedTvShows()
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getAiringTodayTvShows() {
        if (_airingTodayTvShows.value.isLoading) return

        viewModelScope.launch {
            _airingTodayTvShows.update { it.copy(isLoading = true) }

            val stateValue = _airingTodayTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getTvShowItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _airingTodayTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getOnAirTvShows() {
        if (_onAirTvShows.value.isLoading) return

        viewModelScope.launch {
            _onAirTvShows.update { it.copy(isLoading = true) }

            val stateValue = _onAirTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getTvShowItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _onAirTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getPopularTvShows() {
        if (_popularTvShows.value.isLoading) return

        viewModelScope.launch {
            _popularTvShows.update { it.copy(isLoading = true) }

            val stateValue = _popularTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getTvShowItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _popularTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getTopRatedTvShows() {
        if (_topRatedTvShows.value.isLoading) return

        viewModelScope.launch {
            _topRatedTvShows.update { it.copy(isLoading = true) }

            val stateValue = _topRatedTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResponse(
                contentRepository.getTvShowItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _topRatedTvShows.update {
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
    val category: TvShowListCategory
) {
    constructor(category: TvShowListCategory) : this(
        items = emptyList(),
        isLoading = false,
        endReached = false,
        page = 0,
        category = category
    )
}