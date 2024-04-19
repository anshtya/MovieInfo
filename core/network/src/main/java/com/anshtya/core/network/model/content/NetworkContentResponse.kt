package com.anshtya.core.network.model.content

import com.squareup.moshi.Json

data class NetworkContentResponse(
    val page: Int,
    val results: List<NetworkContentItem>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)
