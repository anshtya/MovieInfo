package com.anshtya.movieinfo.core.network.model.details.people

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCrew(
    @SerialName("credit_id") val creditId: String,
    val department: String?,
    val id: Int,
    val job: String?,
    val name: String,
    @SerialName("profile_path") val profilePath: String?
)