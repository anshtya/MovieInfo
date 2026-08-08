package com.anshtya.movieinfo.core.network.model.details.people

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCast(
    val character: String?,
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?
)