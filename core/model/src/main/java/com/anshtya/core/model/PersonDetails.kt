package com.anshtya.core.model

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