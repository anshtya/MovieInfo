package com.anshtya.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val youNavigationRoute = "you"

fun NavGraphBuilder.youScreen() {
    composable(route = youNavigationRoute) {
        YouRoute()
    }
}

fun NavController.navigateToYou(navOptions: NavOptions) {
    navigate(youNavigationRoute, navOptions)
}