package com.anshtya.core.network.util

import java.text.SimpleDateFormat
import java.util.Locale

internal fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return date?.let { outputFormat.format(it) } ?: dateString
}