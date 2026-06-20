package com.anshtya.movieinfo.feature.details

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data class Details(val id: String)

@Serializable
private data object DetailsScreen

@Serializable
private data object Credits

fun NavGraphBuilder.detailsScreen(
    navController: NavController,
    navigateToAuth: () -> Unit
) {
    navigation<Details>(
        startDestination = DetailsScreen
    ) {
        composable<DetailsScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<Details>()
            }
            val viewModel = hiltViewModel<DetailsViewModel>(parentEntry)

            DetailsRoute(
                onBackClick = navController::navigateUp,
                onItemClick = navController::navigateToDetails,
                onSeeAllCastClick = navController::navigateToCredits,
                navigateToAuth = navigateToAuth,
                viewModel = viewModel
            )
        }

        composable<Credits> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<Details>()
            }
            val viewModel = hiltViewModel<DetailsViewModel>(parentEntry)

            CreditsRoute(
                viewModel = viewModel,
                onItemClick = navController::navigateToDetails,
                onBackClick = navController::navigateUp
            )
        }
    }
}

fun NavController.navigateToDetails(id: String) {
    navigate(Details(id))
}

private fun NavController.navigateToCredits() {
    navigate(Credits)
}