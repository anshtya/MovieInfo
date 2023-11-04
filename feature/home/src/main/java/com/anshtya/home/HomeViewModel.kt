package com.anshtya.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {
    /*
    as on tmdb home page
    1. all 4 endpoints
    2. hotstar top view (popular)
    3. search
    */


    val trendingMovies = homeRepository.getTrendingMovies("day")
        .cachedIn(viewModelScope)

}