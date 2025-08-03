package com.anshtya.movieinfo.data.network.model.details.people

import com.anshtya.movieinfo.data.model.details.people.Cast
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCast(
    val character: String?,
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?
) {
    fun asModel() = Cast(
        character = character ?: "",
        id = id,
        name = name,
        profilePath = profilePath ?: ""
    )
}
