package com.anshtya.search

import androidx.annotation.StringRes

internal enum class SearchResultTab(@StringRes val displayName: Int) {
    MOVIES(R.string.movies), TV(R.string.tv)
}