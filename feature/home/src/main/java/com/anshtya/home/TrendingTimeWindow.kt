package com.anshtya.home

import androidx.annotation.StringRes

enum class TrendingTimeWindow(
    @StringRes val uiLabel: Int,
    val parameter: String
) {
    TODAY(R.string.today, "day"),
    THIS_WEEK(R.string.this_week, "week")
}