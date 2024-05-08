package com.anshtya.movieinfo.core.network.model.details.people

import com.anshtya.movieinfo.core.model.details.people.Cast
import com.squareup.moshi.Json

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
