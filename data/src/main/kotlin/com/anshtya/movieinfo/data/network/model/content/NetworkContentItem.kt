package com.anshtya.movieinfo.data.network.model.content

import com.anshtya.movieinfo.data.model.content.ContentItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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