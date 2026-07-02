package com.anshtya.movieinfo.core.network.model.content

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkContentItem(
    val id: Int,
    val name: String?,
    @Json(name = "poster_path") val posterPath: String?,
    val title: String?,
)