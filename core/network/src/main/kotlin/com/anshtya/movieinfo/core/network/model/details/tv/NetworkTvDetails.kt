package com.anshtya.movieinfo.core.network.model.details.tv

import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.anshtya.movieinfo.core.network.model.details.NetworkCredits
import com.anshtya.movieinfo.core.network.model.details.NetworkGenre
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCompany
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCountry
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkTvDetails(
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "created_by") val createdBy: List<NetworkCreatedBy>,
    val credits: NetworkCredits,
    @Json(name = "episode_run_time") val episodeRunTime: List<Int>,
    @Json(name = "first_air_date") val firstAirDate: String,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @Json(name = "in_production") val inProduction: Boolean,
    @Json(name = "last_air_date") val lastAirDate: String,
    @Json(name = "last_episode_to_air") val lastEpisodeToAir: NetworkEpisodeDetails,
    val name: String,
    val networks: List<NetworkBroadcastNetwork>,
    @Json(name = "next_episode_to_air") val nextEpisodeToAir: NetworkEpisodeDetails?,
    @Json(name = "number_of_episodes") val numberOfEpisodes: Int,
    @Json(name = "number_of_seasons") val numberOfSeasons: Int,
    @Json(name = "origin_country") val originCountry: List<String>,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @Json(name = "production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
)