package com.anshtya.movieinfo.core.network.model.details

import com.anshtya.movieinfo.core.model.details.MovieDetails
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.anshtya.movieinfo.core.network.util.formatDate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Locale

@JsonClass(generateAdapter = true)
data class NetworkMovieDetails(
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
//    val belongs_to_collection: Any,
    val budget: Int,
    val credits: NetworkCredits,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @Json(name = "production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
    @Json(name = "release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val tagline: String,
    val title: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
) {
    fun asModel() = MovieDetails(
        adult = adult,
        backdropPath = backdropPath ?: "",
        budget = "%,d".format(budget),
        credits = credits.asModel(),
        genres = genres?.map { it.name } ?: emptyList(),
        id = id,
        originalLanguage = Locale(originalLanguage).displayLanguage,
        overview = overview,
        posterPath = posterPath ?: "",
        productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
        productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
        rating = voteAverage / 2,
        recommendations = recommendations.results.map(NetworkContentItem::asModel),
        releaseDate = formatDate(releaseDate),
        releaseYear = releaseDate.split("-").first().toInt(),
        revenue = "%,d".format(revenue),
        runtime = getFormattedRuntime(),
        tagline = tagline,
        title = title,
        voteCount = voteCount
    )

    private fun getFormattedRuntime(): String {
        val hours = runtime.div(60)
        val minutes = runtime.mod(60)
        return if (minutes < 1) {
            "${hours}h"
        } else {
            "${hours}h ${minutes}m"
        }
    }
}