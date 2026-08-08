package com.anshtya.movieinfo.data.util

import java.text.SimpleDateFormat
import java.util.Locale

internal fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return date?.let { outputFormat.format(it) } ?: dateString
}

internal fun getFormattedMovieRuntime(runtime: Int): String {
    val hours = runtime.div(60)
    val minutes = runtime.mod(60)
    return if (minutes < 1) {
        "${hours}h"
    } else {
        "${hours}h ${minutes}m"
    }
}

internal fun getFormattedTvRuntime(episodeRunTime: List<Int>): String {
    if (episodeRunTime.isEmpty()) return ""

    val hours = episodeRunTime.first().div(60)
    val minutes = episodeRunTime.first().mod(60)
    return if (hours < 1) {
        "${minutes}m"
    } else if (minutes < 1) {
        "${hours}h"
    } else {
        "${hours}h ${minutes}m"
    }
}