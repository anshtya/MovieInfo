package com.anshtya.feature.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.anshtya.feature.you.library_items.LibraryItemsRoute

private const val youNavigationGraphRoute = "you_nav_graph"
private const val youNavigationRoute = "you"
private const val libraryItemsNavigationRoute = "library_items"
const val libraryItemTypeNavigationArgument = "type"

fun NavGraphBuilder.youGraph(
    onBackClick: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToLibraryItem: (String) -> Unit,
    onNavigateToDetails: (String) -> Unit,
) {
    navigation(
        route = youNavigationGraphRoute,
        startDestination = youNavigationRoute
    ) {
        composable(route = youNavigationRoute) {
            YouRoute(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToLibraryItem = onNavigateToLibraryItem
            )
        }
        composable(
            route = "$libraryItemsNavigationRoute/{$libraryItemTypeNavigationArgument}",
            arguments = listOf(
                navArgument(libraryItemTypeNavigationArgument) { type = NavType.StringType }
            )
        ) {
            LibraryItemsRoute(
                onBackClick = onBackClick,
                onNavigateToDetails = onNavigateToDetails
            )
        }
    }
}

fun NavController.navigateToYou(navOptions: NavOptions) {
    navigate(youNavigationRoute, navOptions)
}

fun NavController.navigateToLibraryItem(type: String) {
    navigate("$libraryItemsNavigationRoute/$type")
}