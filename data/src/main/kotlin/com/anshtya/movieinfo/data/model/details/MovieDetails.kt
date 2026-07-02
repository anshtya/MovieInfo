package com.anshtya.movieinfo.data.model.details

import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.details.NetworkMovieDetails
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.content.ContentItem
import com.anshtya.movieinfo.data.model.content.asModel
import com.anshtya.movieinfo.data.model.details.people.Credits
import com.anshtya.movieinfo.data.model.details.people.asModel
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.data.util.formatDate
import com.anshtya.movieinfo.data.util.getFormattedMovieRuntime
import java.util.Locale

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String,
//    val belongs_to_collection: Any,
    val budget: String,
    val credits: Credits,
    val genres: List<String>,
    val id: Int,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Double,
    val recommendations: List<ContentItem>,
    val releaseDate: String,
    val releaseYear: Int,
    val revenue: String,
    val runtime: String,
    val tagline: String,
    val title: String,
    val voteCount: Int
)

fun NetworkMovieDetails.asModel() = MovieDetails(
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
    runtime = getFormattedMovieRuntime(runtime),
    tagline = tagline,
    title = title,
    voteCount = voteCount
)

fun MovieDetails.asLibraryItem() = LibraryItem(
    id = id,
    imagePath = posterPath,
    name = title,
    mediaType = MediaType.MOVIE.name.lowercase()
)