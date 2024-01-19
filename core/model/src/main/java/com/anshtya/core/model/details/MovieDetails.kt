package com.anshtya.core.model.details

import com.anshtya.core.model.MediaType
import com.anshtya.core.model.library.LibraryItem

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String,
//    val belongs_to_collection: Any,
    val budget: String,
    val genres: String,
    val id: Int,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Double,
    val releaseDate: String,
    val releaseYear: Int,
    val revenue: String,
    val runtime: String,
    val tagline: String,
    val title: String,
    val voteCount: Int
)

fun MovieDetails.asLibraryItem() = LibraryItem(
    imagePath = posterPath,
    mediaId = id,
    mediaType = MediaType.MOVIE.toString(),
    name = title,
)
