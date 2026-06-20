package com.anshtya.movieinfo.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
private data object Auth

fun NavGraphBuilder.authScreen(
    onBackClick: () -> Unit,
) {
    composable<Auth> {
        AuthRoute(onBackClick = onBackClick)
    }
}

fun NavController.navigateToAuth() {
    navigate(Auth)
}
