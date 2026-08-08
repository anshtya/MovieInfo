package com.anshtya.movieinfo.core.network.model.details

import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovieDetails(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
//    val belongs_to_collection: Any,
    val budget: Int,
    val credits: NetworkCredits,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @SerialName("production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
    @SerialName("release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val tagline: String,
    val title: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)