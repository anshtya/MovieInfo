package com.anshtya.movieinfo.feature.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    tvShowRepository: TvShowRepository
) : ViewModel() {
    val airingTodayTvShows = tvShowRepository.getAiringTodayTvShows()
        .cachedIn(viewModelScope)

    val onAirTvShows = tvShowRepository.getOnAirTvShows()
        .cachedIn(viewModelScope)

    val popularTvShows = tvShowRepository.getPopularTvShows()
        .cachedIn(viewModelScope)

    val topRatedTvShows = tvShowRepository.getTopRatedTvShows()
        .cachedIn(viewModelScope)
}