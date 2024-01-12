package com.anshtya.core.network.model

import com.anshtya.core.model.EpisodeDetails
import com.squareup.moshi.Json

data class NetworkEpisodeDetails(
    @field:Json(name = "air_date") val airDate: String,
    @field:Json(name = "episode_number") val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    @field:Json(name = "production_code") val productionCode: String,
    val runtime: Int?,
    @field:Json(name = "season_number") val seasonNumber: Int,
    @field:Json(name = "show_id") val showId: Int,
    @field:Json(name = "still_path") val stillPath: String?,
    @field:Json(name = "vote_average") val voteAverage: Double,
    @field:Json(name = "vote_count") val voteCount: Int
)

fun NetworkEpisodeDetails.asModel() = EpisodeDetails(
    airDate = airDate,
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