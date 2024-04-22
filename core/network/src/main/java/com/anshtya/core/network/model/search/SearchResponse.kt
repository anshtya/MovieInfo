package com.anshtya.core.network.model.search

import com.squareup.moshi.Json

data class SearchResponse(
    val page: Int,
    val results: List<NetworkSearchItem>,
    @Json(name = "total_pages") val totalPages: Int
)