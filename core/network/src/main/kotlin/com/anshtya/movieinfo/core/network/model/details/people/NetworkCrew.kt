package com.anshtya.movieinfo.core.network.model.details.people

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCrew(
    @Json(name = "credit_id") val creditId: String,
    val department: String?,
    val id: Int,
    val job: String?,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?
)