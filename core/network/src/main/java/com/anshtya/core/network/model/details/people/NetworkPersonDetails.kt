package com.anshtya.core.network.model.details.people

import com.anshtya.core.model.details.people.PersonDetails
import com.anshtya.core.network.util.formatDate
import com.squareup.moshi.Json

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
) {

    // According to https://developer.themoviedb.org/reference/person-details#genders
    private fun getGender(): String {
        return when (gender) {
            1 -> "Female"
            2 -> "Male"
            3 -> "Non-binary"
            else -> "Not specified"
        }
    }

    fun asModel() = PersonDetails(
        adult = adult,
        alsoKnownAs = if (alsoKnownAs.isEmpty()) "Unknown" else alsoKnownAs.joinToString(", "),
        biography = if (biography.isNullOrEmpty()) "Not available" else biography,
        birthday = birthday?.let { formatDate(it) } ?: "Unknown",
        deathday = deathday?.let { formatDate(it) },
        gender = getGender(),
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        placeOfBirth = placeOfBirth ?: "Unknown",
        profilePath = profilePath ?: ""
    )
}