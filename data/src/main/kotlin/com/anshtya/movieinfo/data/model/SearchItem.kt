package com.anshtya.movieinfo.data.model

import com.anshtya.movieinfo.core.network.model.search.NetworkSearchItem

data class SearchItem(
    val id: Int,
    val name: String,
    val mediaType: String,
    val imagePath: String
)

fun NetworkSearchItem.asModel() = SearchItem(
    id = id,
    name = title ?: name ?: "",
    mediaType = mediaType,
    imagePath = posterPath ?: profilePath ?: ""
)
