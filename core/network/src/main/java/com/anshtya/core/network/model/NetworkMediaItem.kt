package com.anshtya.core.network.model

import com.squareup.moshi.Json

data class NetworkMediaItem(
    val id: Int,
    val name: String?,
    @field:Json(name = "poster_path") val posterPath: String,
    val title: String?,
)