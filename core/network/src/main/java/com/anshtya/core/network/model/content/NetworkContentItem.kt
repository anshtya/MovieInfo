package com.anshtya.core.network.model.content

import com.anshtya.core.model.content.ContentItem
import com.squareup.moshi.Json

data class NetworkContentItem(
    val id: Int,
    val name: String?,
    @Json(name = "poster_path") val posterPath: String?,
    val title: String?,
) {
    fun asModel() = ContentItem(
        id = id,
        imagePath = posterPath ?: "",
        name = name ?: title ?: ""
    )
}