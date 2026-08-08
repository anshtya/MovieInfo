package com.anshtya.movieinfo.data.model.details.people

import com.anshtya.movieinfo.core.network.model.details.people.NetworkPersonDetails
import com.anshtya.movieinfo.data.util.formatDate

data class PersonDetails(
    val adult: Boolean,
    val alsoKnownAs: String,
    val biography: String,
    val birthday: String,
    val deathday: String?,
    val gender: String,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val placeOfBirth: String,
    val profilePath: String
)

fun NetworkPersonDetails.asModel() = PersonDetails(
    adult = adult,
    alsoKnownAs = if (alsoKnownAs.isEmpty()) "Unknown" else alsoKnownAs.joinToString(", "),
    biography = biography.takeIf { !it.isNullOrEmpty() } ?: "Not available",
    birthday = birthday?.let { formatDate(it) } ?: "Unknown",
    deathday = deathday?.let { formatDate(it) },
    gender = getGender(gender),
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    placeOfBirth = placeOfBirth ?: "Unknown",
    profilePath = profilePath ?: ""
)

// According to https://developer.themoviedb.org/reference/person-details#genders
private fun getGender(gender: Int): String {
    return when (gender) {
        1 -> "Female"
        2 -> "Male"
        3 -> "Non-binary"
        else -> "Not specified"
    }
}