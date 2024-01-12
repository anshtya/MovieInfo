package com.anshtya.core.network.model

import com.anshtya.core.model.CreatedBy
import com.anshtya.core.model.TvDetails
import com.squareup.moshi.Json
import java.util.Locale

data class NetworkTvDetails(
    val adult: Boolean,
    @field:Json(name = "backdrop_path") val backdropPath: String,
    @field:Json(name = "created_by") val createdBy: List<CreatedBy>,
    @field:Json(name = "episode_run_time") val episodeRunTime: List<Int>,
    @field:Json(name = "first_air_date") val firstAirDate: String,
    val genres: List<NetworkGenre>,
    val id: Int,
    @field:Json(name = "in_production") val inProduction: Boolean,
    @field:Json(name = "last_air_date") val lastAirDate: String,
    @field:Json(name = "last_episode_to_air") val lastEpisodeToAir: NetworkEpisodeDetails,
    val name: String,
    val networks: List<NetworkBroadcastNetwork>,
    @field:Json(name = "next_episode_to_air") val nextEpisodeToAir: NetworkEpisodeDetails?,
    @field:Json(name = "number_of_episodes") val numberOfEpisodes: Int,
    @field:Json(name = "number_of_seasons") val numberOfSeasons: Int,
    @field:Json(name = "origin_country") val originCountry: List<String>,
    @field:Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    @field:Json(name = "poster_path") val posterPath: String,
    @field:Json(name = "production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @field:Json(name = "production_countries") val productionCountries: List<NetworkProductionCountry>,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    @field:Json(name = "vote_average") val voteAverage: Double,
    @field:Json(name = "vote_count") val voteCount: Int
) {
    fun getFormattedRuntime(): String {
        val hours = episodeRunTime.first().div(60)
        val minutes = episodeRunTime.first().mod(60)
        return if (hours < 1) {
            "${minutes}m"
        } else if (minutes < 1) {
            "${hours}h"
        } else {
            "${hours}h ${minutes}m"
        }
    }
}

fun NetworkTvDetails.asModel() = TvDetails(
    adult = adult,
    backdropPath = backdropPath,
    createdBy = createdBy,
    episodeRunTime = getFormattedRuntime(),
    firstAirDate = firstAirDate,
    genres = genres.joinToString(separator = ", ") { it.name },
    id = id,
    inProduction = if (inProduction) "Yes" else "No",
    lastAirDate = lastAirDate,
    lastEpisodeToAir = lastEpisodeToAir.asModel(),
    name = name,
    networks = networks.joinToString(separator = ", ") { it.name },
    nextEpisodeToAir = nextEpisodeToAir?.asModel(),
    numberOfEpisodes = numberOfEpisodes,
    numberOfSeasons = numberOfSeasons,
    originCountry = originCountry,
    originalLanguage = Locale(originalLanguage).displayLanguage,
    overview = overview,
    posterPath = posterPath,
    productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
    productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
    releaseYear = firstAirDate.split("-").first().toInt(),
    status = status,
    tagline = tagline,
    type = type,
    rating = voteAverage/2,
    voteCount = voteCount
)