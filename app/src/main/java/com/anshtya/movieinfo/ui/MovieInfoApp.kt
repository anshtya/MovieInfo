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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.anshtya.movieinfo.navigation.MovieInfoDestination
import com.anshtya.movieinfo.navigation.MovieInfoNavigation

@Composable
fun MovieInfoApp(
    navController: NavHostController = rememberNavController()
) {
    val destinations = MovieInfoDestination.entries.toList()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        bottomBar = {
            MovieInfoNavigationBar(
                destinations = destinations,
                currentRoute = currentRoute
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
    currentRoute: String?
) {
    NavigationBar {
        destinations.forEach {
            val selected = currentRoute == it.route
            NavigationBarItem(
                selected = selected,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        imageVector = if (selected) it.selectedIcon else it.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(it.titleId))}
            )
        }
    }
}