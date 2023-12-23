package com.anshtya.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.model.PopularContentType
import com.anshtya.data.repository.ContentRepository
import com.anshtya.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _trendingContentFilters = TrendingTimeWindow.entries.toList()
    val trendingContentFilters = _trendingContentFilters.map { it.uiLabel }

    private val _popularContentFilters = PopularContentFilter.entries.toList()
    val popularContentFilters = _popularContentFilters.map { it.uiLabel }

    private val _freeContentFilters = FreeContentType.entries.toList()
    val freeContentFilters = _freeContentFilters.map { it.uiLabel }

    val selectedTrendingContentFilterIndex = userDataRepository.trendingContentFilterIndex
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val selectedPopularContentFilterIndex = userDataRepository.popularContentFilterIndex
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val selectedFreeContentFilterIndex = userDataRepository.freeContentFilterIndex
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val trendingMovies = selectedTrendingContentFilterIndex
        .flatMapLatest { selectedIndex ->
            contentRepository.getTrendingMovies(
                _trendingContentFilters[selectedIndex].toParameter()
            )
        }
        .cachedIn(viewModelScope)

    val popularContent = selectedPopularContentFilterIndex
        .flatMapLatest { selectedIndex ->
            contentRepository.getPopularContent(
                _popularContentFilters[selectedIndex].toParameter()
            )
        }
        .cachedIn(viewModelScope)

    val freeContent = selectedFreeContentFilterIndex
        .flatMapLatest { selectedIndex ->
            contentRepository.getFreeContent(
                _freeContentFilters[selectedIndex].toParameter()
            )
        }
        .cachedIn(viewModelScope)

    fun setTrendingContentFilterIndex(index: Int) {
        viewModelScope.launch {
            userDataRepository.setTrendingContentFilterIndex(index)
        }
    }

    fun setPopularContentFilterIndex(index: Int) {
        viewModelScope.launch {
            userDataRepository.setPopularContentFilterIndex(index)
        }
    }

    fun setFreeContentFilterIndex(index: Int) {
        viewModelScope.launch {
            userDataRepository.setFreeContentFilterIndex(index)
        }
    }

    private fun TrendingTimeWindow.toParameter(): String {
        return when(this) {
            TrendingTimeWindow.TODAY -> "day"
            TrendingTimeWindow.THIS_WEEK -> "week"
        }
    }

    private fun FreeContentType.toParameter(): String {
        return when (this) {
            FreeContentType.MOVIES -> "movie"
            FreeContentType.TV -> "tv"
        }
    }

    private fun PopularContentFilter.toParameter(): PopularContentType {
        return when (this) {
            PopularContentFilter.STREAMING -> PopularContentType.STREAMING
            PopularContentFilter.IN_THEATRES -> PopularContentType.IN_THEATRES
            PopularContentFilter.FOR_RENT -> PopularContentType.FOR_RENT
        }
    }
}