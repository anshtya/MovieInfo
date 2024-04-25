package com.anshtya.movieinfo.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val onboardingNavigationRoute = "onboarding"
fun NavGraphBuilder.onboardingScreen(navController: NavController) {
    navigation(
        route = onboardingNavigationRoute,
        startDestination = OnboardingDestinations.MAIN
    ) {
        composable(
            route = OnboardingDestinations.MAIN
        ) {
            OnboardingMainScreen(
                navigateToAuth = navController::navigateToOnboardingAuth
            )
        }

//        composable(
//            route = OnboardingDestinations.AUTH
//        ) {
//            OnboardingAuthRoute()
//        }
    }
}

object OnboardingDestinations {
    const val MAIN = "onboarding_main"
    const val AUTH = "onboarding_auth"
}