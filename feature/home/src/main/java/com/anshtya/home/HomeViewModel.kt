package com.anshtya.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.model.PopularContentType
import com.anshtya.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _timeWindowOptions = TrendingTimeWindow.entries.toList()
    val timeWindowOptions = _timeWindowOptions.map { it.uiLabel }

    private val _popularContentFilters = PopularContentFilter.entries.toList()
    val popularContentFilters = _popularContentFilters.map { it.uiLabel }

    private val _freeContentTypes = FreeContentType.entries.toList()
    val freeContentTypes = _freeContentTypes.map { it.uiLabel }

    private val _selectedTimeWindow = MutableStateFlow(TrendingTimeWindow.TODAY)
    val selectedTimeWindowIndex = _selectedTimeWindow
        .mapLatest { timeWindow ->
            _timeWindowOptions.indexOf(timeWindow)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    private val _selectedContentFilter = MutableStateFlow(PopularContentFilter.STREAMING)
    val selectedContentFilterIndex = _selectedContentFilter
        .mapLatest { contentFilter ->
            _popularContentFilters.indexOf(contentFilter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    private val _selectedFreeContent = MutableStateFlow(FreeContentType.MOVIES)
    val selectedFreeContentIndex = _selectedFreeContent
        .mapLatest { contentType ->
            _freeContentTypes.indexOf(contentType)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val trendingMovies = _selectedTimeWindow
        .flatMapLatest { timeWindow ->
            contentRepository.getTrendingMovies(timeWindow.toParameter())
        }
        .cachedIn(viewModelScope)

    val popularContent = _selectedContentFilter
        .flatMapLatest { contentFilter ->
            contentRepository.getPopularContent(contentFilter.toContentTypeParameter())
        }
        .cachedIn(viewModelScope)

    val freeContent = _selectedFreeContent
        .flatMapLatest { contentType ->
            contentRepository.getFreeContent(contentType.toParameter())
        }
        .cachedIn(viewModelScope)

    fun setTrendingTimeWindow(timeWindowIndex: Int) {
        _selectedTimeWindow.update { _timeWindowOptions[timeWindowIndex] }
    }

    fun setPopularContentFilter(contentFilterIndex: Int) {
        _selectedContentFilter.update { _popularContentFilters[contentFilterIndex] }
    }

    fun setFreeContentType(contentTypeIndex: Int) {
        _selectedFreeContent.update { _freeContentTypes[contentTypeIndex] }
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

    private fun PopularContentFilter.toContentTypeParameter(): PopularContentType {
        return when (this) {
            PopularContentFilter.STREAMING -> PopularContentType.STREAMING
            PopularContentFilter.IN_THEATRES -> PopularContentType.IN_THEATRES
            PopularContentFilter.FOR_RENT -> PopularContentType.FOR_RENT
        }
    }
}