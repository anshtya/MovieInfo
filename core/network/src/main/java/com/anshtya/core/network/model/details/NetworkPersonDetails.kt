package com.anshtya.core.network.model.details

import com.anshtya.core.model.details.PersonDetails
import com.squareup.moshi.Json

data class NetworkPersonDetails(
    val adult: Boolean,
    @field:Json(name = "also_known_as") val alsoKnownAs: List<String>,
    val biography: String,
    val birthday: String,
    val deathday: String?,
    val gender: Int,
    val id: Int,
    @field:Json(name = "known_for_department") val knownForDepartment: String,
    val name: String,
    @field:Json(name = "place_of_birth") val placeOfBirth: String,
    @field:Json(name = "profile_path") val profilePath: String
) {

    // According to https://developer.themoviedb.org/reference/person-details#genders
    fun getGender(): String {
        return when (gender) {
            1 -> "Female"
            2 -> "Male"
            3 -> "Non-binary"
            else -> "Not specified"
        }
    }
}

fun NetworkPersonDetails.asModel() = PersonDetails(
    adult = adult,
    alsoKnownAs = alsoKnownAs.joinToString(", "),
    biography = biography,
    birthday = birthday,
    deathday = deathday,
    gender = getGender(),
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    placeOfBirth = placeOfBirth,
    profilePath = profilePath
)