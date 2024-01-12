package com.anshtya.core.network.model

import com.anshtya.core.model.SearchItem
import com.squareup.moshi.Json

data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val title: String?,
    @field:Json(name = "media_type") val mediaType: String,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "profile_path") val profilePath: String?,
)

fun NetworkSearchItem.asModel() = SearchItem(
    id = id,
    name = title ?: name ?: "",
    mediaType = mediaType,
    imagePath = posterPath ?: profilePath ?: ""
)