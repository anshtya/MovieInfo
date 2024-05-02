package com.anshtya.core.network.model.details.people

import com.anshtya.core.model.details.people.Crew
import com.squareup.moshi.Json

data class NetworkCrew(
    @Json(name = "credit_id") val creditId: String,
    val department: String?,
    val id: Int,
    val job: String?,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?
) {
    fun asModel() = Crew(
        creditId = creditId,
        department = department ?: "",
        id = id,
        job = job ?: "",
        name = name,
        profilePath = profilePath ?: ""
    )
}
