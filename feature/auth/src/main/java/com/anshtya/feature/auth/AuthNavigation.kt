package com.anshtya.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val authScreenNavigationRoute = "auth"

fun NavGraphBuilder.authScreen(onLogIn: () -> Unit) {
    composable(route = authScreenNavigationRoute) {
        AuthRoute(onLogIn = onLogIn)
    }
}

fun NavController.navigateToAuth() {
    navigate(authScreenNavigationRoute)
}