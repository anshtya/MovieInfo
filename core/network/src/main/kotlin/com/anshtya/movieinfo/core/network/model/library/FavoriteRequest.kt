package com.anshtya.movieinfo.core.network.model.library

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val favorite: Boolean
)