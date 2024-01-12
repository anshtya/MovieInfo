package com.anshtya.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home"

fun NavGraphBuilder.homeScreen(onNavigateToDetail: (String) -> Unit) {
    composable(route = homeNavigationRoute) {
        HomeRoute(onItemClick = onNavigateToDetail)
    }
}

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(homeNavigationRoute, navOptions)
}