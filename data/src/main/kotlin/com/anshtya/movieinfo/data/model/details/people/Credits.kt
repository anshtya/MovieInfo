package com.anshtya.movieinfo.data.model.details.people

import com.anshtya.movieinfo.core.network.model.details.NetworkCredits
import com.anshtya.movieinfo.core.network.model.details.people.NetworkCast
import com.anshtya.movieinfo.core.network.model.details.people.NetworkCrew

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>
)

fun NetworkCredits.asModel() = Credits(
    cast = cast.map(NetworkCast::asModel),
    crew = crew.map(NetworkCrew::asModel)
)