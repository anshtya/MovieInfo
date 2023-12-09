package com.anshtya.network.model

import com.squareup.moshi.Json

data class MultiSearchResponse(
    val results: List<NetworkSearchSuggestion>
)

data class NetworkSearchSuggestion(
    val id: Int,
    val title: String?,
    val name: String?,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "profile_path") val profilePath: String?,
)
