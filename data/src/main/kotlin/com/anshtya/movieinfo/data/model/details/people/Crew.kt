package com.anshtya.movieinfo.data.model.details.people

import com.anshtya.movieinfo.core.network.model.details.people.NetworkCrew

data class Crew(
    val creditId: String,
    val department: String,
    val id: Int,
    val job: String,
    val name: String,
    val profilePath: String
)

fun NetworkCrew.asModel() = Crew(
    creditId = creditId,
    department = department ?: "",
    id = id,
    job = job ?: "",
    name = name,
    profilePath = profilePath ?: ""
)