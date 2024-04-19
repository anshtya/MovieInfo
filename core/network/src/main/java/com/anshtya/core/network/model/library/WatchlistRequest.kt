package com.anshtya.core.network.model.library

import com.squareup.moshi.Json

data class WatchlistRequest(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "media_id") val mediaId: Int,
    val watchlist: Boolean
)