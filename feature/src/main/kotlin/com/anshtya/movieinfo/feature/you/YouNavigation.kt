package com.anshtya.movieinfo.feature.you

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anshtya.movieinfo.feature.you.library_items.LibraryItemsRoute
import kotlinx.serialization.Serializable

@Serializable
data object YouGraph

@Serializable
private data object You

@Serializable
internal data class LibraryItems(val type: String)

fun NavGraphBuilder.youScreen(
    navController: NavController,
    navigateToAuth: () -> Unit,
    navigateToDetails: (String) -> Unit,
) {
    navigation<YouGraph>(
        startDestination = You
    ) {
        composable<You> {
            YouRoute(
                navigateToAuth = navigateToAuth,
                navigateToLibraryItem = navController::navigateToLibraryItem
            )
        }
        composable<LibraryItems> {
            LibraryItemsRoute(
                onBackClick = navController::navigateUp,
                navigateToDetails = navigateToDetails
            )
        }
    }
}

fun NavController.navigateToYou(navOptions: NavOptions) {
    navigate(You, navOptions)
}

fun NavController.navigateToLibraryItem(type: String) {
    navigate(LibraryItems(type))
}
