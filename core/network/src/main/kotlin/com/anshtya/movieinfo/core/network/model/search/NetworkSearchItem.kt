package com.anshtya.movieinfo.core.network.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val title: String?,
    @SerialName("media_type") val mediaType: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("profile_path") val profilePath: String?,
)