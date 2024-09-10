package com.anshtya.movieinfo.feature.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.anshtya.movieinfo.feature.you.library_items.LibraryItemsRoute

private const val youNavigationGraphRoute = "you_nav_graph"
private const val youNavigationRoute = "you"
private const val libraryItemsNavigationRoute = "library_items"
const val libraryItemTypeNavigationArgument = "type"

fun NavGraphBuilder.youScreen(
    navController: NavController,
    navigateToAuth: () -> Unit,
    navigateToDetails: (String) -> Unit,
) {
    navigation(
        route = youNavigationGraphRoute,
        startDestination = youNavigationRoute
    ) {
        composable(route = youNavigationRoute) {
            YouRoute(
                navigateToAuth = navigateToAuth,
                navigateToLibraryItem = navController::navigateToLibraryItem
            )
        }
        composable(
            route = "$libraryItemsNavigationRoute/{$libraryItemTypeNavigationArgument}",
            arguments = listOf(
                navArgument(libraryItemTypeNavigationArgument) { type = NavType.StringType }
            )
        ) {
            LibraryItemsRoute(
                onBackClick = navController::navigateUp,
                navigateToDetails = navigateToDetails
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