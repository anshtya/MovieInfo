package com.anshtya.movieinfo.core.network.model.details

import com.anshtya.movieinfo.core.network.model.details.people.NetworkCast
import com.anshtya.movieinfo.core.network.model.details.people.NetworkCrew
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCredits(
    val cast: List<NetworkCast>,
    val crew: List<NetworkCrew>
)