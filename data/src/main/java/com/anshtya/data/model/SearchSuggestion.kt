package com.anshtya.data.model

import com.anshtya.network.model.NetworkSearchSuggestion

data class SearchSuggestion(
    val id: Int,
    val name: String
)

fun NetworkSearchSuggestion.asModel() = SearchSuggestion(
    id = id,
    name = title.takeIf { it != null } ?: name!!
)