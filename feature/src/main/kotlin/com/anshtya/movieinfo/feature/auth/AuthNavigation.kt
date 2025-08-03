package com.anshtya.movieinfo.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val authScreenNavigationRoute = "auth"

fun NavGraphBuilder.authScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = authScreenNavigationRoute
    ) {
        AuthRoute(onBackClick = onBackClick)
    }
}

fun NavController.navigateToAuth() {
    navigate(authScreenNavigationRoute)
}