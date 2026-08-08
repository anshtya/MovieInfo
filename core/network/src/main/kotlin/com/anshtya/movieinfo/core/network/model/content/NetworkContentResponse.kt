package com.anshtya.movieinfo.core.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkContentResponse(
    val page: Int,
    val results: List<NetworkContentItem>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)
