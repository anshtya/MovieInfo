package com.anshtya.movieinfo.core.network.model.search

import com.anshtya.movieinfo.core.model.SearchItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val title: String?,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "profile_path") val profilePath: String?,
)

fun NetworkSearchItem.asModel() = SearchItem(
    id = id,
    name = title ?: name ?: "",
    mediaType = mediaType,
    imagePath = posterPath ?: profilePath ?: ""
)