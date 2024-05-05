package com.anshtya.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val searchNavigationRoute = "search"

fun NavGraphBuilder.searchScreen(navigateToDetail: (String) -> Unit) {
    composable(route = searchNavigationRoute) {
        SearchRoute(navigateToDetail = navigateToDetail)
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions) {
    navigate(searchNavigationRoute, navOptions)
}