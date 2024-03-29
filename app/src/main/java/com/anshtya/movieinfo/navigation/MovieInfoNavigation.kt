package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.anshtya.feature.auth.authScreen
import com.anshtya.feature.auth.navigateToAuth
import com.anshtya.feature.details.detailsScreen
import com.anshtya.feature.details.navigateToDetails
import com.anshtya.feature.home.homeNavigationRoute
import com.anshtya.feature.home.homeScreen
import com.anshtya.feature.search.searchScreen
import com.anshtya.feature.you.navigateToLibraryItem
import com.anshtya.feature.you.youGraph

@Composable
fun MovieInfoNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute
    ) {
        homeScreen(onNavigateToDetail = navController::navigateToDetails)
        searchScreen(onNavigateToDetail = navController::navigateToDetails)
        youGraph(
            onBackClick = navController::popBackStack,
            onNavigateToAuth = navController::navigateToAuth,
            onNavigateToLibraryItem = navController::navigateToLibraryItem,
            onNavigateToDetails = navController::navigateToDetails
        )

        authScreen(onLogIn = navController::popBackStack)
        detailsScreen()
    }
}