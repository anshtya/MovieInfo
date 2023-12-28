package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.anshtya.feature.home.homeScreen
import com.anshtya.feature.home.homeNavigationRoute
import com.anshtya.feature.search.searchScreen
import com.anshtya.feature.you.youScreen

@Composable
fun MovieInfoNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute
    ) {
        homeScreen()
        searchScreen()
        youScreen()
    }
}