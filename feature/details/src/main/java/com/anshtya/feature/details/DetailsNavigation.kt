package com.anshtya.feature.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val detailsNavigationRoute = "details"
internal const val idNavigationArgument = "id"

fun NavGraphBuilder.detailsScreen() {
    composable(
        route = "$detailsNavigationRoute/{$idNavigationArgument}",
        arguments = listOf(
            navArgument(idNavigationArgument) { type = NavType.StringType }
        )
    ) {
        DetailsRoute()
    }
}

fun NavController.navigateToDetails(id: String) {
    navigate("$detailsNavigationRoute/$id")
}