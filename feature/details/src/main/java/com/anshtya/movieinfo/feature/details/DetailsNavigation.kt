package com.anshtya.movieinfo.feature.details

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

private const val detailsNavigationRoute = "details"
private const val creditsNavigationRoute = "credits"
internal const val idNavigationArgument = "id"
private const val detailsNavigationRouteWithArg = "$detailsNavigationRoute/{$idNavigationArgument}"

fun NavGraphBuilder.detailsScreen(
    navController: NavController,
    navigateToAuth: () -> Unit
) {
    navigation(
        route = detailsNavigationRouteWithArg,
        startDestination = detailsNavigationRoute
    ) {
        composable(
            route = detailsNavigationRoute
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(detailsNavigationRouteWithArg)
            }
            val viewModel = hiltViewModel<DetailsViewModel>(parentEntry)

            DetailsRoute(
                onBackClick = navController::popBackStack,
                onItemClick = navController::navigateToDetails,
                onSeeAllCastClick = navController::navigateToCredits,
                navigateToAuth = navigateToAuth,
                viewModel = viewModel
            )
        }

        composable(
            route = creditsNavigationRoute
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(detailsNavigationRouteWithArg)
            }
            val viewModel = hiltViewModel<DetailsViewModel>(parentEntry)

            CreditsRoute(
                viewModel = viewModel,
                onItemClick = navController::navigateToDetails,
                onBackClick = navController::popBackStack
            )
        }
    }
}

fun NavController.navigateToDetails(id: String) {
    navigate("$detailsNavigationRoute/$id")
}

private fun NavController.navigateToCredits() {
    navigate(creditsNavigationRoute)
}