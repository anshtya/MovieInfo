package com.anshtya.core.network.model.details

import com.anshtya.core.model.details.MovieDetails
import com.squareup.moshi.Json
import java.util.Locale

data class NetworkMovieDetails(
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
//    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<NetworkGenre>,
    val id: Int,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @Json(name = "production_countries") val productionCountries: List<NetworkProductionCountry>,
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
        genres = genres.joinToString(separator = ", ") { it.name },
        id = id,
        originalLanguage = Locale(originalLanguage).displayLanguage,
        overview = overview,
        posterPath = posterPath ?: "",
        productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
        productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
        rating = voteAverage/2,
        releaseDate = releaseDate,
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