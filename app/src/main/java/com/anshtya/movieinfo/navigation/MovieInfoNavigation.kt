package com.anshtya.movieinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.anshtya.feature.auth.authScreen
import com.anshtya.feature.auth.navigateToAuth
import com.anshtya.feature.auth.navigateToAuthFromOnboarding
import com.anshtya.feature.details.detailsScreen
import com.anshtya.feature.details.navigateToDetails
import com.anshtya.movieinfo.feature.movies.moviesNavigationRoute
import com.anshtya.movieinfo.feature.movies.moviesScreen
import com.anshtya.feature.search.searchScreen
import com.anshtya.feature.you.youScreen
import com.anshtya.movieinfo.feature.movies.navigateToMovies
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
        onboardingScreen(navigateToAuth = navController::navigateToAuthFromOnboarding)
        authScreen(
            navigateToMovies = {
                navController.navigateToMovies(
                    navOptions {
                        popUpTo(onboardingNavigationRoute) { inclusive = true }
                    }
                )
            },
            onBackClick = navController::popBackStack
        )
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