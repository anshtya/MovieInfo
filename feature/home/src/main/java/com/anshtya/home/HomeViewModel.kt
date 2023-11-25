package com.anshtya.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    /*
    as on tmdb home page
    1. all 4 endpoints
    2. top view (popular)
    3. search
    */

    private val _trendingTimeWindow = savedStateHandle
        .getStateFlow(TRENDING_TIME_WINDOW, "day")

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingMovies =_trendingTimeWindow.flatMapLatest {
        homeRepository.getTrendingMovies(it)
            .cachedIn(viewModelScope)
    }

    fun setTrendingTimeWindow(timeWindow: String) {
        savedStateHandle[TRENDING_TIME_WINDOW] = timeWindow
    }

}
const val TRENDING_TIME_WINDOW = "trendingTimeWindow"