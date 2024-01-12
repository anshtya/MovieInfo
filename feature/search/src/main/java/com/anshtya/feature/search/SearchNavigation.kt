package com.anshtya.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val searchNavigationRoute = "search"

fun NavGraphBuilder.searchScreen(onNavigateToDetail: (String) -> Unit) {
    composable(route = searchNavigationRoute) {
        SearchRoute(onSearchResultClick = onNavigateToDetail)
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions) {
    navigate(searchNavigationRoute, navOptions)
}