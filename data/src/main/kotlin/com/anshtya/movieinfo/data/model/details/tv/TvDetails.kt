package com.anshtya.movieinfo.data.model.details.tv

import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.details.tv.NetworkCreatedBy
import com.anshtya.movieinfo.core.network.model.details.tv.NetworkTvDetails
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.content.ContentItem
import com.anshtya.movieinfo.data.model.content.asModel
import com.anshtya.movieinfo.data.model.details.people.Credits
import com.anshtya.movieinfo.data.model.details.people.asModel
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.data.util.formatDate
import com.anshtya.movieinfo.data.util.getFormattedTvRuntime
import java.util.Locale

data class TvDetails(
    val adult: Boolean,
    val backdropPath: String,
    val createdBy: List<CreatedBy>,
    val credits: Credits,
    val episodeRunTime: String,
    val firstAirDate: String,
    val genres: List<String>,
    val id: Int,
    val inProduction: String,
    val lastAirDate: String,
    val lastEpisodeToAir: EpisodeDetails,
    val name: String,
    val networks: String,
    val nextEpisodeToAir: EpisodeDetails?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Double,
    val recommendations: List<ContentItem>,
    val releaseYear: Int,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    val voteCount: Int
)

fun NetworkTvDetails.asModel() = TvDetails(
    adult = adult,
    backdropPath = backdropPath ?: "",
    createdBy = createdBy.map(NetworkCreatedBy::asModel),
    credits = credits.asModel(),
    episodeRunTime = getFormattedTvRuntime(episodeRunTime),
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

fun TvDetails.asLibraryItem() = LibraryItem(
    id = id,
    imagePath = posterPath,
    name = name,
    mediaType = MediaType.TV.name.lowercase(),
)