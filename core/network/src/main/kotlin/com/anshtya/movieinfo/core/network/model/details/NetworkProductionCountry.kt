package com.anshtya.movieinfo.core.network.model.details

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductionCountry(
    val iso_3166_1: String,
    val name: String
)