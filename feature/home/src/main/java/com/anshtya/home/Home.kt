package com.anshtya.home

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val trendingMovies = homeViewModel.trendingMovies.collectAsLazyPagingItems()
}