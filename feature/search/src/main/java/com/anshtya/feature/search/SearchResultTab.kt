package com.anshtya.feature.search

import androidx.annotation.StringRes

internal enum class SearchResultTab(@StringRes val displayName: Int) {
    MOVIES(R.string.movies), TV(R.string.tv)
}