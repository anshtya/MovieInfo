package com.anshtya.movieinfo.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anshtya.movieinfo.feature.auth.authScreen
import com.anshtya.movieinfo.feature.auth.navigateToAuth

const val onboardingNavigationGraphRoute = "onboardingGraph"
private const val onboardingNavigationRoute = "onboarding"

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavHostController,
    navigateToMainGraph: () -> Unit
) {
    navigation(
        route = onboardingNavigationGraphRoute,
        startDestination = onboardingNavigationRoute
    ) {
        composable(
            route = onboardingNavigationRoute
        ) {
            OnboardingScreen(navigateToAuth = navController::navigateToAuth)
        }
        authScreen(
            navigateToMovies = navigateToMainGraph,
            onBackClick = navController::popBackStack
        )
    }
}