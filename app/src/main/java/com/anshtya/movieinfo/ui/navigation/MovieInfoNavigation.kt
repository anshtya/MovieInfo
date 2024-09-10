package com.anshtya.movieinfo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anshtya.movieinfo.feature.auth.authScreen
import com.anshtya.movieinfo.feature.auth.navigateToAuth
import com.anshtya.movieinfo.feature.details.detailsScreen
import com.anshtya.movieinfo.feature.details.navigateToDetails
import com.anshtya.movieinfo.feature.movies.moviesNavigationRoute
import com.anshtya.movieinfo.feature.movies.moviesScreen
import com.anshtya.movieinfo.feature.search.searchScreen
import com.anshtya.movieinfo.feature.tv.tvShowsScreen
import com.anshtya.movieinfo.feature.you.youScreen
import com.anshtya.movieinfo.ui.OnboardingScreen
import com.anshtya.movieinfo.ui.onboardingNavigationRoute

@Composable
fun MovieInfoNavigation(
    hideOnboarding: Boolean,
    navController: NavHostController
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
        composable(
            route = onboardingNavigationRoute
        ) {
            OnboardingScreen(navigateToAuth = navController::navigateToAuth)
        }

        authScreen(onBackClick = navController::navigateUp)

        moviesScreen(
            navController = navController,
            navigateToDetails = navController::navigateToDetails
        )

        tvShowsScreen(
            navController = navController,
            navigateToDetails = navController::navigateToDetails
        )

        searchScreen(navigateToDetail = navController::navigateToDetails)

        youScreen(
            navController = navController,
            navigateToAuth = navController::navigateToAuth,
            navigateToDetails = navController::navigateToDetails
        )

        detailsScreen(
            navController = navController,
            navigateToAuth = navController::navigateToAuth
        )
    }
}