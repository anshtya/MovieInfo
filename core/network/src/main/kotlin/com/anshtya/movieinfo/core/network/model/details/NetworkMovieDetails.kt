package com.anshtya.movieinfo.core.network.model.details

import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
)