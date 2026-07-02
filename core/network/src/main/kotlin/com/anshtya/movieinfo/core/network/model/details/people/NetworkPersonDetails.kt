package com.anshtya.movieinfo.core.network.model.details.people

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPersonDetails(
    val adult: Boolean,
    @Json(name = "also_known_as") val alsoKnownAs: List<String>,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val gender: Int,
    val id: Int,
    @Json(name = "known_for_department") val knownForDepartment: String,
    val name: String,
    @Json(name = "place_of_birth") val placeOfBirth: String?,
    @Json(name = "profile_path") val profilePath: String?
)