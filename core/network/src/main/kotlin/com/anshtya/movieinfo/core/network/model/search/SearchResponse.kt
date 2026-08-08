package com.anshtya.movieinfo.core.network.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val page: Int,
    val results: List<NetworkSearchItem>,
    @SerialName("total_pages") val totalPages: Int
)