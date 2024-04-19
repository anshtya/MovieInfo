package com.anshtya.core.network.model.library

import com.squareup.moshi.Json

data class FavoriteRequest(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "media_id") val mediaId: Int,
    val favorite: Boolean
)