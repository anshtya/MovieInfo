package com.anshtya.feature.you.library_items

import androidx.annotation.StringRes
import com.anshtya.feature.you.R

enum class LibraryMediaType(
    @StringRes val displayName: Int
) {
    MOVIE(R.string.movies),
    TV(R.string.tv_shows)
}