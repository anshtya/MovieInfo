package com.anshtya.movieinfo.feature.movies

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
data object Movies

@Serializable
private data object MoviesFeed

@Serializable
private data class MoviesItems(val category: String)

fun NavGraphBuilder.moviesScreen(
    navController: NavController,
    navigateToDetails: (String) -> Unit
) {
    navigation<Movies>(
        startDestination = MoviesFeed
    ) {
        composable<MoviesFeed> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Movies)
            }
            val viewModel = hiltViewModel<MoviesViewModel>(parentEntry)
            FeedRoute(
                navigateToDetails = navigateToDetails,
                navigateToItems = { navController.navigateToMoviesItems(it) },
                viewModel = viewModel,
            )
        }

        composable<MoviesItems> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Movies)
            }
            val viewModel = hiltViewModel<MoviesViewModel>(parentEntry)
            val args = backStackEntry.toRoute<MoviesItems>()
            ItemsRoute(
                categoryName = args.category,
                onItemClick = navigateToDetails,
                onBackClick = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}

fun NavController.navigateToMovies(navOptions: NavOptions) {
    navigate(Movies, navOptions)
}

fun NavController.navigateToMoviesItems(category: String) {
    navigate(MoviesItems(category))
}
