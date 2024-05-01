package com.anshtya.core.network.model.details.tv

import com.anshtya.core.model.details.tv.EpisodeDetails
import com.anshtya.core.network.util.formatDate
import com.squareup.moshi.Json

data class NetworkEpisodeDetails(
    @Json(name = "air_date") val airDate: String,
    @Json(name = "episode_number") val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "production_code") val productionCode: String,
    val runtime: Int?,
    @Json(name = "season_number") val seasonNumber: Int,
    @Json(name = "show_id") val showId: Int,
    @Json(name = "still_path") val stillPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
) {
    fun asModel() = EpisodeDetails(
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
}