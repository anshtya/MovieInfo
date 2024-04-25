package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.anshtya.feature.auth.authScreen
import com.anshtya.feature.auth.navigateToAuth
import com.anshtya.feature.details.detailsScreen
import com.anshtya.feature.details.navigateToDetails
import com.anshtya.movieinfo.feature.movies.moviesNavigationRoute
import com.anshtya.movieinfo.feature.movies.moviesScreen
import com.anshtya.feature.search.searchScreen
import com.anshtya.feature.you.navigateToLibraryItem
import com.anshtya.feature.you.youGraph
import com.anshtya.movieinfo.feature.tv.tvShowsScreen
import com.anshtya.movieinfo.onboarding.onboardingNavigationRoute
import com.anshtya.movieinfo.onboarding.onboardingScreen

@Composable
fun MovieInfoNavigation(
    navController: NavHostController,
    hideOnboarding: Boolean
) {
    val startDestination = if (hideOnboarding) {
        moviesNavigationRoute
    } else {
        onboardingNavigationRoute
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        onboardingScreen(navController = navController)
        moviesScreen(
            navController = navController,
            onNavigateToDetail = navController::navigateToDetails
        )
        tvShowsScreen(
            navController = navController,
            onNavigateToDetail = navController::navigateToDetails
        )
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