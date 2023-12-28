package com.anshtya.core.network.model

import com.squareup.moshi.Json

data class NetworkStreamingItem(
    val id: Long,
    val name: String?,
    val overview: String?,
    @field:Json(name = "poster_path") val posterPath: String,
    val title: String?,
)