package com.anshtya.movieinfo.data.model.content

import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem

data class ContentItem(
    val id: Int,
    val imagePath: String,
    val name: String
)

fun NetworkContentItem.asModel() = ContentItem(
    id = id,
    imagePath = posterPath ?: "",
    name = name ?: title ?: ""
)