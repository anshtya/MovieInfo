package com.anshtya.movieinfo.core.network.model.details.tv

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCreatedBy(
    val id: Int,
    val name: String
)
