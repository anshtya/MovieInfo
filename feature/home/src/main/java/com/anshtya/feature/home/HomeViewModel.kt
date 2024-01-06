package com.anshtya.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.core.model.FreeContentType
import com.anshtya.core.model.PopularContentType
import com.anshtya.core.model.TrendingContentTimeWindow
import com.anshtya.data.repository.ContentRepository
import com.anshtya.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {
    private var shouldReload = false

    private val _includeAdult = userDataRepository.userData
        .map { it.includeAdultResults }
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    private val _trendingContentFilters = TrendingContentFilter.entries
    val trendingContentFilters = _trendingContentFilters.map { it.uiText }.toImmutableList()

    private val _popularContentFilters = PopularContentFilter.entries
    val popularContentFilters = _popularContentFilters.map { it.uiText }.toImmutableList()

    private val _freeContentFilters = FreeContentFilter.entries
    val freeContentFilters = _freeContentFilters.map { it.uiText }.toImmutableList()

    val selectedTrendingContentFilterIndex = userDataRepository.userData
        .map {
            val filter = when (it.selectedTrendingContentTimeWindow) {
                TrendingContentTimeWindow.DAY -> TrendingContentFilter.TODAY
                TrendingContentTimeWindow.WEEK -> TrendingContentFilter.THIS_WEEK
            }
            _trendingContentFilters.indexOf(filter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val selectedPopularContentFilterIndex = userDataRepository.userData
        .map {
            val filter = when (it.selectedPopularContentType) {
                PopularContentType.STREAMING -> PopularContentFilter.STREAMING
                PopularContentType.IN_THEATRES -> PopularContentFilter.IN_THEATRES
                PopularContentType.FOR_RENT -> PopularContentFilter.FOR_RENT
            }
            _popularContentFilters.indexOf(filter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val selectedFreeContentFilterIndex = userDataRepository.userData
        .map {
            val filter = when (it.selectedFreeContentType) {
                FreeContentType.MOVIE -> FreeContentFilter.MOVIES
                FreeContentType.TV -> FreeContentFilter.TV
            }
            _freeContentFilters.indexOf(filter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    val trendingMovies = selectedTrendingContentFilterIndex
        .flatMapLatest { index ->
            contentRepository.getTrendingMovies(
                timeWindow = _trendingContentFilters[index].toTimeWindow(),
                shouldReload = shouldReload
            )
        }
        .cachedIn(viewModelScope)

    val popularContent = selectedPopularContentFilterIndex
        .flatMapLatest { index ->
            contentRepository.getPopularContent(
                contentType = _popularContentFilters[index].toContentType(),
                includeAdult = _includeAdult.first(),
                shouldReload = shouldReload
            )
        }
        .cachedIn(viewModelScope)

    val freeContent = selectedFreeContentFilterIndex
        .flatMapLatest { index ->
            contentRepository.getFreeContent(
                contentType = _freeContentFilters[index].toContentType(),
                includeAdult = _includeAdult.first(),
                shouldReload = shouldReload
            )
        }
        .cachedIn(viewModelScope)

    fun setTrendingContentFilter(index: Int) {
        shouldReload = true
        viewModelScope.launch {
            val filter = _trendingContentFilters[index]
            userDataRepository.setTrendingContentPreference(filter.toTimeWindow())
        }
    }

    fun setPopularContentFilter(index: Int) {
        shouldReload = true
        viewModelScope.launch {
            val filter = _popularContentFilters[index]
            userDataRepository.setPopularContentPreference(filter.toContentType())
        }
    }

    fun setFreeContentFilter(index: Int) {
        shouldReload = true
        viewModelScope.launch {
            val filter = _freeContentFilters[index]
            userDataRepository.setFreeContentPreference(filter.toContentType())
        }
    }
}