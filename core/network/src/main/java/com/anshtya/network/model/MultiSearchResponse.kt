package com.anshtya.network.model

data class MultiSearchResponse(
    val results: List<NetworkSearchSuggestion>
)

data class NetworkSearchSuggestion(
    val id: Int,
    val title: String?,
    val name: String?
)
