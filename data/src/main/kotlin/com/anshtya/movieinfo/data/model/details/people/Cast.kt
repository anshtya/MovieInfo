package com.anshtya.movieinfo.data.model.details.people

import com.anshtya.movieinfo.core.network.model.details.people.NetworkCast

data class Cast(
    val character: String,
    val id: Int,
    val name: String,
    val profilePath: String
)

fun NetworkCast.asModel() = Cast(
    character = character ?: "",
    id = id,
    name = name,
    profilePath = profilePath ?: ""
)