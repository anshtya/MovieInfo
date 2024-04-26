package com.anshtya.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val authScreenNavigationRoute = "auth"

fun NavGraphBuilder.authScreen(
    navigateToMovies: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = "${authScreenNavigationRoute}?hideOnboarding={hideOnboarding}",
        arguments = listOf(navArgument("hideOnboarding") { defaultValue = true })
    ) { backStackEntry ->
        val hideOnboarding = backStackEntry.arguments?.getBoolean("hideOnboarding")
        AuthRoute(
            hideOnboarding = hideOnboarding,
            navigateToMovies = navigateToMovies,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToAuth() {
    navigate(authScreenNavigationRoute)
}

fun NavController.navigateToAuthFromOnboarding() {
    navigate("${authScreenNavigationRoute}?hideOnboarding=${false}")
}