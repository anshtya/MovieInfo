package com.anshtya.movieinfo.data.network.model.content

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkContentResponse(
    val page: Int,
    val results: List<NetworkContentItem>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)
