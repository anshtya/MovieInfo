package com.anshtya.data.model

import com.anshtya.core.network.model.NetworkSearchSuggestion

data class SearchSuggestion(
    val id: Int,
    val name: String,
    val imagePath: String
)

fun NetworkSearchSuggestion.asModel() = SearchSuggestion(
    id = id,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    },
    imagePath = when {
        !posterPath.isNullOrEmpty() -> posterPath!!
        !profilePath.isNullOrEmpty() -> profilePath!!
        else -> ""
    }
)