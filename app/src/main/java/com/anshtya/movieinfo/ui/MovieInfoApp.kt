package com.anshtya.movieinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.anshtya.feature.home.navigateToHome
import com.anshtya.movieinfo.navigation.MovieInfoDestination
import com.anshtya.movieinfo.navigation.MovieInfoNavigation
import com.anshtya.feature.search.navigateToSearch
import com.anshtya.feature.you.navigateToYou

@Composable
fun MovieInfoApp(
    navController: NavHostController = rememberNavController()
) {
    val destinations = MovieInfoDestination.entries.toList()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            MovieInfoNavigationBar(
                destinations = destinations,
                currentDestination = currentDestination,
                onNavigateToDestination = { destination ->
                    navController.navigateToDestination(destination)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MovieInfoNavigation(navController)
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

private fun NavController.navigateToDestination(destination: MovieInfoDestination) {
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (destination) {
        MovieInfoDestination.HOME -> navigateToHome(navOptions)
        MovieInfoDestination.SEARCH -> navigateToSearch(navOptions)
        MovieInfoDestination.MY_LIBRARY -> {}
        MovieInfoDestination.YOU -> navigateToYou(navOptions)
    }
}