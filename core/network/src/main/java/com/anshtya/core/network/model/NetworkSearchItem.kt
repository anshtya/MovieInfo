package com.anshtya.core.network.model

import com.anshtya.core.model.SearchItem
import com.squareup.moshi.Json

data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val overview: String?,
    @field:Json(name = "poster_path") val posterPath: String?,
    val title: String?,
)

fun NetworkSearchItem.asModel() = SearchItem(
    id = id,
    name = when {
        !title.isNullOrEmpty() -> title
        !name.isNullOrEmpty() -> name
        else -> ""
    },
    overview = overview ?: "",
    posterPath = posterPath ?: ""
)