package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.anshtya.home.homeScreen
import com.anshtya.home.homeNavigationRoute
import com.anshtya.search.navigateToSearchResults
import com.anshtya.search.searchScreen

@Composable
fun MovieInfoNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute
    ) {
        homeScreen()
        searchScreen(
            onSearch = navController::navigateToSearchResults,
            onBackClick = { navController.popBackStack() }
        )
    }
}