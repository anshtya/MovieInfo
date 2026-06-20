package com.anshtya.movieinfo.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Search

fun NavGraphBuilder.searchScreen(navigateToDetail: (String) -> Unit) {
    composable<Search> {
        SearchRoute(navigateToDetail = navigateToDetail)
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions) {
    navigate(Search, navOptions)
}