package com.anshtya.movieinfo.feature.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anshtya.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {
    val nowPlayingMovies = movieRepository.getNowPlayingMovies()
        .cachedIn(viewModelScope)

    val popularMovies = movieRepository.getPopularMovies()
        .cachedIn(viewModelScope)

    val topRatedMovies = movieRepository.getTopRatedMovies()
        .cachedIn(viewModelScope)

    val upcomingMovies = movieRepository.getUpcomingMovies()
        .cachedIn(viewModelScope)
}