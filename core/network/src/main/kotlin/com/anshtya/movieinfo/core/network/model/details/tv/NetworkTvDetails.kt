package com.anshtya.movieinfo.core.network.model.details.tv

import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.anshtya.movieinfo.core.network.model.details.NetworkCredits
import com.anshtya.movieinfo.core.network.model.details.NetworkGenre
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCompany
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCountry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTvDetails(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("created_by") val createdBy: List<NetworkCreatedBy>,
    val credits: NetworkCredits,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>,
    @SerialName("first_air_date") val firstAirDate: String,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @SerialName("in_production") val inProduction: Boolean,
    @SerialName("last_air_date") val lastAirDate: String,
    @SerialName("last_episode_to_air") val lastEpisodeToAir: NetworkEpisodeDetails,
    val name: String,
    val networks: List<NetworkBroadcastNetwork>,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: NetworkEpisodeDetails?,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int,
    @SerialName("number_of_seasons") val numberOfSeasons: Int,
    @SerialName("origin_country") val originCountry: List<String>,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @SerialName("production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)