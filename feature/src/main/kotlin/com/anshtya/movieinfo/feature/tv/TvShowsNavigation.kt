package com.anshtya.movieinfo.feature.tv

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data object TvShows

@Serializable
private data object TvShowsFeed

@Serializable
private data class TvShowsItems(val category: String)

fun NavGraphBuilder.tvShowsScreen(
    navController: NavController,
    navigateToDetails: (String) -> Unit
) {
    navigation<TvShows>(
        startDestination = TvShowsFeed
    ) {
        composable<TvShowsFeed> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(TvShows)
            }
            val viewModel = hiltViewModel<TvShowsViewModel>(parentEntry)
            FeedRoute(
                navigateToDetails = navigateToDetails,
                navigateToItems = { navController.navigateToTvShowsItems(it) },
                viewModel = viewModel,
            )
        }

        composable<TvShowsItems> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(TvShows)
            }
            val viewModel = hiltViewModel<TvShowsViewModel>(parentEntry)
            val args = backStackEntry.toRoute<TvShowsItems>()
            ItemsRoute(
                categoryName = args.category,
                onItemClick = navigateToDetails,
                onBackClick = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}

fun NavController.navigateToTvShows(navOptions: NavOptions) {
    navigate(TvShows, navOptions)
}

fun NavController.navigateToTvShowsItems(category: String) {
    navigate(TvShowsItems(category))
}
