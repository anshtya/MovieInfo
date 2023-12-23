package com.anshtya.data.model

import com.anshtya.local.database.entity.FreeContentEntity
import com.anshtya.network.model.NetworkStreamingItem

data class FreeItem(
    val id: Long,
    val imagePath: String,
    val name: String,
    val overview: String
)

fun NetworkStreamingItem.asFreeContentEntity() = FreeContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    },
    overview = overview ?: ""
)

fun FreeContentEntity.asModel() = FreeItem(
    id = remoteId,
    imagePath = imagePath,
    name = name,
    overview = overview
)