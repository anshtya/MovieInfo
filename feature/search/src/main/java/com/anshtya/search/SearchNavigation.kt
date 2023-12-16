package com.anshtya.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val searchQueryArgument = "query"
const val searchNavigationRoute = "search/?$searchQueryArgument={$searchQueryArgument}"

fun NavGraphBuilder.searchScreen(
    onSearch: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        route = searchNavigationRoute,
        arguments = listOf(navArgument(searchQueryArgument) {
            type = NavType.StringType
            defaultValue = ""
        })
    ) {
        SearchRoute(
            onSearch = onSearch,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToSearchResults(query: String = "") {
    navigate("search/$query")
}

fun NavController.navigateToSearch(navOptions: NavOptions) {
    navigate(searchNavigationRoute, navOptions)
}