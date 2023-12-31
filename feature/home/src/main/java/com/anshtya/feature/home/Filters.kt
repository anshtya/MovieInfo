package com.anshtya.feature.home

import androidx.annotation.StringRes
import com.anshtya.core.model.FreeContentType
import com.anshtya.core.model.PopularContentType
import com.anshtya.core.model.TrendingContentTimeWindow

enum class TrendingContentFilter(
    @StringRes val uiText: Int
) {
    TODAY(R.string.today),
    THIS_WEEK(R.string.this_week);

    fun toTimeWindow(): TrendingContentTimeWindow {
        return when (this) {
            TODAY -> TrendingContentTimeWindow.DAY
            THIS_WEEK -> TrendingContentTimeWindow.WEEK
        }
    }
}

enum class PopularContentFilter(
    @StringRes val uiText: Int
) {
    STREAMING(R.string.streaming),
    IN_THEATRES(R.string.in_theatres),
    FOR_RENT(R.string.for_rent);

    fun toContentType(): PopularContentType {
        return when (this) {
            STREAMING -> PopularContentType.STREAMING
            IN_THEATRES -> PopularContentType.IN_THEATRES
            FOR_RENT -> PopularContentType.FOR_RENT
        }
    }
}

enum class FreeContentFilter(
    @StringRes val uiText: Int
) {
    MOVIES(R.string.movies),
    TV(R.string.tv);

    fun toContentType(): FreeContentType {
        return when (this) {
            MOVIES -> FreeContentType.MOVIE
            TV -> FreeContentType.TV
        }
    }
}