package com.anshtya.movieinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.anshtya.movieinfo.feature.movies.navigateToMovies
import com.anshtya.movieinfo.feature.search.navigateToSearch
import com.anshtya.movieinfo.feature.tv.navigateToTvShows
import com.anshtya.movieinfo.feature.you.navigateToYou
import com.anshtya.movieinfo.ui.navigation.MovieInfoDestination
import com.anshtya.movieinfo.ui.navigation.MovieInfoNavigation

@Composable
fun MovieInfoApp(
    hideOnboarding: Boolean,
    navController: NavHostController = rememberNavController()
) {
    val bottomBarDestinations = remember { MovieInfoDestination.entries }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = bottomBarDestinations.any { destination ->
        currentDestination?.route?.contains(destination.name, true) ?: false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MovieInfoNavigationBar(
                    destinations = bottomBarDestinations,
                    currentDestination = currentDestination,
                    onNavigateToDestination = { destination ->
                        navController.navigateToBottomBarDestination(destination)
                    }
                )
            }
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MovieInfoNavigation(
                hideOnboarding = hideOnboarding,
                navController = navController
            )
        }
    }
}

@Composable
fun MovieInfoNavigationBar(
    destinations: List<MovieInfoDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (MovieInfoDestination) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(id = destination.titleId)) }
            )
        }
    }
}

private fun NavDestination?.isDestinationInHierarchy(destination: MovieInfoDestination): Boolean {
    return this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
}

private fun NavController.navigateToBottomBarDestination(destination: MovieInfoDestination) {
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (destination) {
        MovieInfoDestination.MOVIES -> navigateToMovies(navOptions)
        MovieInfoDestination.TV_SHOWS -> navigateToTvShows(navOptions)
        MovieInfoDestination.SEARCH -> navigateToSearch(navOptions)
        MovieInfoDestination.YOU -> navigateToYou(navOptions)
    }
}