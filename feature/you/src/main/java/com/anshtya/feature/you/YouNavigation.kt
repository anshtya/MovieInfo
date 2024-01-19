package com.anshtya.feature.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

private const val youNavigationGraphRoute = "you_nav_graph"
private const val youNavigationRoute = "you"
private const val libraryItemsNavigationRoute = "library_items"

fun NavGraphBuilder.youGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToLibraryItems: () -> Unit
) {
    navigation(
        route = youNavigationGraphRoute,
        startDestination = youNavigationRoute
    ) {
        composable(route = youNavigationRoute) {
            YouRoute(onNavigateToAuth = onNavigateToAuth)
        }
        composable(route = libraryItemsNavigationRoute) {
            LibraryItemsRoute()
        }
    }
}

fun NavController.navigateToYou(navOptions: NavOptions) {
    navigate(youNavigationRoute, navOptions)
}

fun NavController.navigateToLibraryItems() {
    navigate(libraryItemsNavigationRoute)
}