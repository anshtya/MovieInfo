package com.anshtya.data.model

import com.anshtya.core.local.database.entity.FreeContentEntity
import com.anshtya.core.network.model.NetworkMediaItem

fun NetworkMediaItem.asFreeContentEntity() = FreeContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    }
)