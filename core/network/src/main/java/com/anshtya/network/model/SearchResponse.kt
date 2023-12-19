package com.anshtya.network.model

import com.squareup.moshi.Json

data class SearchResponse(
    val page: Int,
    val results: List<NetworkSearchItem>,
    @field:Json(name = "total_pages") val totalPages: Int
)