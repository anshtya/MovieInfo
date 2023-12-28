package com.anshtya.feature.home

import androidx.annotation.StringRes

internal enum class TrendingTimeWindow(
    @StringRes val uiLabel: Int
) {
    TODAY(R.string.today),
    THIS_WEEK(R.string.this_week)
}

internal enum class PopularContentFilter(
    @StringRes val uiLabel: Int
) {
    STREAMING(R.string.streaming),
    IN_THEATRES(R.string.in_theatres),
    FOR_RENT(R.string.for_rent),
}

internal enum class FreeContentType(
    @StringRes val uiLabel: Int
) {
    MOVIES(R.string.movies),
    TV(R.string.tv)
}