package com.anshtya.feature.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val youNavigationRoute = "com/anshtya/feature/you"

fun NavGraphBuilder.youScreen(onNavigateToAuth: () -> Unit) {
    composable(route = youNavigationRoute) {
        YouRoute(onNavigateToAuth = onNavigateToAuth)
    }
}

fun NavController.navigateToYou(navOptions: NavOptions) {
    navigate(youNavigationRoute, navOptions)
}