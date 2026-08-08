package com.anshtya.movieinfo.core.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkContentItem(
    val id: Int,
    val name: String?,
    @SerialName("poster_path") val posterPath: String?,
    val title: String?,
)