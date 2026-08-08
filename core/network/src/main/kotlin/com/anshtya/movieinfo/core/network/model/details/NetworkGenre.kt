package com.anshtya.movieinfo.core.network.model.details

import kotlinx.serialization.Serializable

@Serializable
data class NetworkGenre(
    val id: Int,
    val name: String
)