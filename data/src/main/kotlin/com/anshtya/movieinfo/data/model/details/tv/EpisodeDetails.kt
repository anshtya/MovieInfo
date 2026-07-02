package com.anshtya.movieinfo.data.model.details.tv

import com.anshtya.movieinfo.core.network.model.details.tv.NetworkEpisodeDetails
import com.anshtya.movieinfo.data.util.formatDate

data class EpisodeDetails(
    val airDate: String,
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val productionCode: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String,
    val voteAverage: Double,
    val voteCount: Int
)

fun NetworkEpisodeDetails.asModel() = EpisodeDetails(
    airDate = formatDate(airDate),
    episodeNumber = episodeNumber,
    id = id,
    name = name,
    overview = overview,
    productionCode = productionCode,
    runtime = runtime,
    seasonNumber = seasonNumber,
    showId = showId,
    stillPath = stillPath ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount
)