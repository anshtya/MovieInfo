package com.anshtya.movieinfo.core.network.model.details.people

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCast(
    val character: String?,
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?
)