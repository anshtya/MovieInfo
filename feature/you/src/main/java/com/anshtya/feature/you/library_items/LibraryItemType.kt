package com.anshtya.feature.you.library_items

import androidx.annotation.StringRes
import com.anshtya.feature.you.R

enum class LibraryItemType(
    @StringRes val displayName: Int
) {
    FAVORITES(R.string.favorites),
    WATCHLIST(R.string.watchlist)
}