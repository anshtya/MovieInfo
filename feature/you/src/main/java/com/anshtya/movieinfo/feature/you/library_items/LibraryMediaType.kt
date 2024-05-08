package com.anshtya.movieinfo.feature.you.library_items

import androidx.annotation.StringRes
import com.anshtya.movieinfo.feature.you.R

enum class LibraryMediaType(
    @StringRes val displayName: Int
) {
    MOVIE(R.string.movies),
    TV(R.string.tv_shows)
}