package com.anshtya.network.model

import com.squareup.moshi.Json

data class NetworkStreamingItem(
    val id: Int,
    @field:Json(name = "poster_path") val posterPath: String
)