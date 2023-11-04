package com.anshtya.data.model

import com.anshtya.network.model.NetworkStreamingItem

data class StreamingItem(
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val genreIds: List<Int>,
    val id: Int,
    val originCountry: List<String>,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

fun NetworkStreamingItem.asModel() = StreamingItem(
    adult = adult,
    backdropPath = backdropPath,
    firstAirDate = firstAirDate,
    genreIds = genreIds,
    id = id,
    originCountry = originCountry,
    mediaType = mediaType,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)