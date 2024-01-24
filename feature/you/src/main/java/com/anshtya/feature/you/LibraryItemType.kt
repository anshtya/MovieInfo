package com.anshtya.feature.you

import androidx.annotation.StringRes

enum class LibraryItemType(
    @StringRes val displayName: Int
) {
    FAVORITES(R.string.favorites),
    WATCHLIST(R.string.watchlist)
}