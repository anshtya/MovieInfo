package com.anshtya.data.model

import com.anshtya.core.local.database.entity.PopularContentEntity
import com.anshtya.core.network.model.NetworkStreamingItem

data class PopularItem(
    val id: Long,
    val imagePath: String,
    val name: String,
    val overview: String
)

fun NetworkStreamingItem.asPopularContentEntity() = PopularContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    },
    overview = overview ?: ""
)

fun PopularContentEntity.asModel() = PopularItem(
    id = remoteId,
    imagePath = imagePath,
    name = name,
    overview = overview
)

enum class PopularContentType {
    STREAMING, IN_THEATRES, FOR_RENT
}