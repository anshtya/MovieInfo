package com.anshtya.core.network.model.details

import com.anshtya.core.model.details.people.Credits
import com.anshtya.core.network.model.details.people.NetworkCast
import com.anshtya.core.network.model.details.people.NetworkCrew

data class NetworkCredits(
    val cast: List<NetworkCast>,
    val crew: List<NetworkCrew>
) {
    fun asModel() = Credits(
        cast = cast.map(NetworkCast::asModel),
        crew = crew.map(NetworkCrew::asModel)
    )
}
