package com.anshtya.data.model

import com.anshtya.core.local.database.entity.TrendingContentEntity
import com.anshtya.core.network.model.NetworkStreamingItem

data class TrendingItem(
    val id: Long,
    val imagePath: String,
    val name: String,
    val overview: String
)

fun NetworkStreamingItem.asTrendingContentEntity() = TrendingContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    },
    overview = overview ?: ""
)

fun TrendingContentEntity.asModel() = TrendingItem(
    id = remoteId,
    imagePath = imagePath,
    name = name,
    overview = overview
)