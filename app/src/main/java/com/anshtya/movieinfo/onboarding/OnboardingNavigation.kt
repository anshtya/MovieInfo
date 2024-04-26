package com.anshtya.movieinfo.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val onboardingNavigationRoute = "onboarding"

fun NavGraphBuilder.onboardingScreen(
    navigateToAuth: () -> Unit
) {
    composable(
        route = onboardingNavigationRoute
    ) {
        OnboardingScreen(navigateToAuth = navigateToAuth)
    }
}