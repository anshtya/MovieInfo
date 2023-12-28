package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.anshtya.home.homeScreen
import com.anshtya.home.homeNavigationRoute
import com.anshtya.search.searchScreen
import com.anshtya.you.youScreen

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