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
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val name: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

fun NetworkStreamingItem.asModel() = StreamingItem(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    firstAirDate = firstAirDate ?: "",
    genreIds = genreIds ?: listOf(),
    id = id ?: 0,
    originCountry = originCountry ?: listOf(),
    mediaType = mediaType ?: "",
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    originalName = originalName ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0,
    posterPath = posterPath ?: "",
    releaseDate = releaseDate ?: "",
    title = title ?: "",
    name = name ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0
)