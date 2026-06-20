package com.anshtya.movieinfo.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LiveTv
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.anshtya.movieinfo.R
import com.anshtya.movieinfo.feature.movies.Movies
import com.anshtya.movieinfo.feature.search.Search
import com.anshtya.movieinfo.feature.tv.TvShows
import com.anshtya.movieinfo.feature.you.YouGraph

enum class MovieInfoDestination(
    val route: Any,
    @StringRes val titleId: Int,
    val selectedIcon: ImageVector,
    val icon: ImageVector
) {
    MOVIES(
        route = Movies,
        titleId = R.string.movies,
        selectedIcon = Icons.Rounded.Home,
        icon = Icons.Outlined.Home
    ),
    TV_SHOWS(
        route = TvShows,
        titleId = R.string.tv_shows,
        selectedIcon = Icons.Rounded.LiveTv,
        icon = Icons.Outlined.LiveTv
    ),
    SEARCH(
        route = Search,
        titleId = R.string.search,
        selectedIcon = Icons.Rounded.Search,
        icon = Icons.Outlined.Search
    ),
    YOU(
        route = YouGraph,
        titleId = R.string.you,
        selectedIcon = Icons.Rounded.Person,
        icon = Icons.Outlined.Person
    )
}