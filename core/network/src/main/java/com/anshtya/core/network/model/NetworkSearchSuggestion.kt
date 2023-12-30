package com.anshtya.core.network.model

import com.anshtya.core.model.SearchSuggestion
import com.squareup.moshi.Json

data class NetworkSearchSuggestion(
    val id: Int,
    val title: String?,
    val name: String?,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "profile_path") val profilePath: String?,
)

fun NetworkSearchSuggestion.asModel() = SearchSuggestion(
    id = id,
    name = when {
        !title.isNullOrEmpty() -> title
        !name.isNullOrEmpty() -> name
        else -> ""
    },
    imagePath = when {
        !posterPath.isNullOrEmpty() -> posterPath
        !profilePath.isNullOrEmpty() -> profilePath
        else -> ""
    }
)