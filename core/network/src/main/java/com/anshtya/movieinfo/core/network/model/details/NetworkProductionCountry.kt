package com.anshtya.movieinfo.core.network.model.details

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkProductionCountry(
    val iso_3166_1: String,
    val name: String
)