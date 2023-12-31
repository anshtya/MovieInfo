package com.anshtya.core.model

enum class FreeContentType(val parameter: String) {
    MOVIE("movie"), TV("tv")
}

enum class PopularContentType {
    STREAMING, IN_THEATRES, FOR_RENT
}

enum class TrendingContentTimeWindow(val parameter: String) {
    DAY("day"), WEEK("week")
}