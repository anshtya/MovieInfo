package com.anshtya.core.network.model

import com.anshtya.core.model.MovieDetails
import com.squareup.moshi.Json
import java.util.Locale

data class NetworkMovieDetails(
    val adult: Boolean,
    @field:Json(name = "backdrop_path") val backdropPath: String?,
//    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<NetworkGenre>,
    val id: Int,
    @field:Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    val popularity: Double,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @field:Json(name = "production_countries") val productionCountries: List<NetworkProductionCountry>,
    @field:Json(name = "release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val tagline: String,
    val title: String,
    @field:Json(name = "vote_average") val voteAverage: Double,
    @field:Json(name = "vote_count") val voteCount: Int
) {
    fun getFormattedRuntime(): String {
        val hours = runtime.div(60)
        val minutes = runtime.mod(60)
        return if (minutes < 1) {
            "${hours}h"
        } else {
            "${hours}h ${minutes}m"
        }
    }
}

fun NetworkMovieDetails.asModel() = MovieDetails(
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