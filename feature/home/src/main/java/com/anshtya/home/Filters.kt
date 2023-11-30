package com.anshtya.home

import androidx.annotation.StringRes

enum class TrendingTimeWindow(
    @StringRes val uiLabel: Int
) {
    TODAY(R.string.today),
    THIS_WEEK(R.string.this_week)
}

enum class PopularContentFilter(
    @StringRes val uiLabel: Int
) {
    STREAMING(R.string.streaming),
    IN_THEATRES(R.string.in_theatres),
    FOR_RENT(R.string.for_rent),
}

enum class FreeContentType(
    @StringRes val uiLabel: Int
) {
    MOVIES(R.string.movies),
    TV(R.string.tv)
}