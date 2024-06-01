package com.anshtya.movieinfo.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val authScreenNavigationRoute = "auth"

fun NavGraphBuilder.authScreen(
    onBackClick: () -> Unit,
    navigateToMovies: () -> Unit = {}
) {
    composable(
        route = authScreenNavigationRoute
    ) {
        AuthRoute(
            navigateToMovies = navigateToMovies,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToAuth() {
    navigate(authScreenNavigationRoute)
}