package com.anshtya.movieinfo.data.network.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val page: Int,
    val results: List<NetworkSearchItem>,
    @Json(name = "total_pages") val totalPages: Int
)