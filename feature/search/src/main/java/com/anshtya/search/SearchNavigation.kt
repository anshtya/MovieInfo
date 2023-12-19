package com.anshtya.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val searchNavigationRoute = "search"

fun NavGraphBuilder.searchScreen() {
    composable(
        route = searchNavigationRoute
    ) {
        SearchRoute()
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions) {
    navigate(searchNavigationRoute, navOptions)
}