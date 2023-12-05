package com.anshtya.movieinfo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.anshtya.movieinfo.R

enum class MovieInfoDestination(
    @StringRes val titleId: Int,
    val selectedIcon: ImageVector,
    val icon: ImageVector,
    val route: String
) {
    HOME(
        titleId = R.string.home,
        selectedIcon = Icons.Rounded.Home,
        icon = Icons.Outlined.Home,
        route = "home"
    ),
    MY_LIBRARY(
        titleId = R.string.my_library,
        selectedIcon = Icons.Rounded.Bookmarks,
        icon = Icons.Outlined.Bookmarks,
        route = "library"
    ),
    YOU(
        titleId = R.string.you,
        selectedIcon = Icons.Rounded.Person,
        icon = Icons.Outlined.Person,
        route = "you"
    )
}