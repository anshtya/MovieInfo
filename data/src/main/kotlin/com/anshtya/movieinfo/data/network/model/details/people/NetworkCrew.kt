package com.anshtya.movieinfo.data.network.model.details.people

import com.anshtya.movieinfo.data.model.details.people.Crew
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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
