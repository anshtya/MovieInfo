package com.anshtya.movieinfo.core.network.model.details

import com.anshtya.movieinfo.core.model.details.people.Credits
import com.anshtya.movieinfo.core.network.model.details.people.NetworkCast
import com.anshtya.movieinfo.core.network.model.details.people.NetworkCrew
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCredits(
    val cast: List<NetworkCast>,
    val crew: List<NetworkCrew>
) {
    fun asModel() = Credits(
        cast = cast.map(NetworkCast::asModel),
        crew = crew.map(NetworkCrew::asModel)
    )
}
