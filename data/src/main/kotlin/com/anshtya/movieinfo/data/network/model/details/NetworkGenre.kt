package com.anshtya.movieinfo.data.network.model.details

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkGenre(
    val id: Int,
    val name: String
)