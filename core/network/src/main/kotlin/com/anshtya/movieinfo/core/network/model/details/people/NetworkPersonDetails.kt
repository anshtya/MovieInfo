package com.anshtya.movieinfo.core.network.model.details.people

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPersonDetails(
    val adult: Boolean,
    @SerialName("also_known_as") val alsoKnownAs: List<String>,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerialName("place_of_birth") val placeOfBirth: String?,
    @SerialName("profile_path") val profilePath: String?
)