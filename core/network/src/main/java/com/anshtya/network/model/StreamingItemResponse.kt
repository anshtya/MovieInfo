package com.anshtya.network.model

import com.squareup.moshi.Json

data class StreamingItemResponse(
    val page: Int,
    val results: List<NetworkStreamingItem>,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "total_results") val totalResults: Int
)
