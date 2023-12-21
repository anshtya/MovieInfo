package com.anshtya.network.model

import com.squareup.moshi.Json

data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val overview: String?,
    @field:Json(name = "poster_path") val posterPath: String?,
    val title: String?,
)