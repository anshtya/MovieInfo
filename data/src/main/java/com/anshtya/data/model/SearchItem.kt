package com.anshtya.data.model

import com.anshtya.network.model.NetworkSearchItem

data class SearchItem(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String
)

fun NetworkSearchItem.asModel() = SearchItem(
    id = id,
    name = when {
        title.isNullOrEmpty() -> name!!
        name.isNullOrEmpty() -> title!!
        else -> ""
    },
    overview = overview,
    posterPath = posterPath ?: ""
)