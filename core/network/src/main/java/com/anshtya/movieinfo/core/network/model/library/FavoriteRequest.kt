package com.anshtya.movieinfo.core.network.model.library

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteRequest(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "media_id") val mediaId: Int,
    val favorite: Boolean
)