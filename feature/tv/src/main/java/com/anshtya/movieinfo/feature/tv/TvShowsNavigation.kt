package com.anshtya.movieinfo.feature.tv

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

private const val tvShowsNavigationRoute = "tv_shows"

fun NavGraphBuilder.tvShowsScreen(
    navController: NavController,
    onNavigateToDetail: (String) -> Unit
) {
    navigation(
        route = tvShowsNavigationRoute,
        startDestination = TvShowsScreenRoutes.FEED
    ) {
        composable(route = TvShowsScreenRoutes.FEED) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(tvShowsNavigationRoute)
            }
            val viewModel = hiltViewModel<TvShowsViewModel>(parentEntry)
            FeedRoute(
                onItemClick = onNavigateToDetail,
                onSeeAllClick = { navController.navigate("${TvShowsScreenRoutes.ITEMS}/$it") },
                viewModel = viewModel,
            )
        }

        composable(
            route = "${TvShowsScreenRoutes.ITEMS}/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(tvShowsNavigationRoute)
            }
            val viewModel = hiltViewModel<TvShowsViewModel>(parentEntry)
            ItemsRoute(
                categoryName = backStackEntry.arguments?.getString("category")!!,
                onItemClick = onNavigateToDetail,
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}

internal object TvShowsScreenRoutes {
    const val FEED = "tv_shows_feed"
    const val ITEMS = "tv_shows_items"
}

fun NavController.navigateToTvShows(navOptions: NavOptions) {
    navigate(tvShowsNavigationRoute, navOptions)
}