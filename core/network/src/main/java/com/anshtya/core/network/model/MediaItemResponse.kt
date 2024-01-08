package com.anshtya.core.network.model

import com.squareup.moshi.Json

data class MediaItemResponse(
    val page: Int,
    val results: List<NetworkMediaItem>,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "total_results") val totalResults: Int
)
