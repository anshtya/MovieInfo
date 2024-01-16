package com.anshtya.core.network.model.content

import com.squareup.moshi.Json

data class ContentResponse(
    val page: Int,
    val results: List<NetworkContentItem>,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "total_results") val totalResults: Int
)
