package com.anshtya.movieinfo.core.network.model.details.tv

import com.anshtya.movieinfo.core.model.details.tv.TvDetails
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.anshtya.movieinfo.core.network.model.details.NetworkCredits
import com.anshtya.movieinfo.core.network.model.details.NetworkGenre
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCompany
import com.anshtya.movieinfo.core.network.model.details.NetworkProductionCountry
import com.anshtya.movieinfo.core.network.util.formatDate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Locale

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
) {
    fun asModel() = TvDetails(
        adult = adult,
        backdropPath = backdropPath ?: "",
        createdBy = createdBy.map(NetworkCreatedBy::asModel),
        credits = credits.asModel(),
        episodeRunTime = getFormattedRuntime(),
        firstAirDate = formatDate(firstAirDate),
        genres = genres?.map { it.name } ?: emptyList(),
        id = id,
        inProduction = if (inProduction) "Yes" else "No",
        lastAirDate = formatDate(lastAirDate),
        lastEpisodeToAir = lastEpisodeToAir.asModel(),
        name = name,
        networks = networks.joinToString(separator = ", ") { it.name },
        nextEpisodeToAir = nextEpisodeToAir?.asModel(),
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = Locale(originalLanguage).displayLanguage,
        overview = overview,
        posterPath = posterPath ?: "",
        productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
        productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
        recommendations = recommendations.results.map(NetworkContentItem::asModel),
        releaseYear = firstAirDate.split("-").first().toInt(),
        status = status,
        tagline = tagline,
        type = type,
        rating = voteAverage / 2,
        voteCount = voteCount
    )

    private fun getFormattedRuntime(): String {
        if (episodeRunTime.isEmpty()) return ""

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