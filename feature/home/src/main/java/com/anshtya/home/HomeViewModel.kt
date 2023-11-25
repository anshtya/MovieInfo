package com.anshtya.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository
) : ViewModel() {

    private val _savedTimeWindow = MutableStateFlow(TrendingTimeWindow.TODAY)
    val savedTimeWindow = _savedTimeWindow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingMovies = _savedTimeWindow
        .flatMapLatest { homeRepository.getTrendingMovies(it.parameter) }
        .cachedIn(viewModelScope)

    fun setTrendingTimeWindow(timeWindow: TrendingTimeWindow) {
        _savedTimeWindow.update { timeWindow }
    }
}