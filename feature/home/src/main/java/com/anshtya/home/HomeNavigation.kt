package com.anshtya.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home"

fun NavGraphBuilder.homeScreen() {
    composable(route = homeNavigationRoute) {
        HomeRoute()
    }
}

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(homeNavigationRoute, navOptions)
}