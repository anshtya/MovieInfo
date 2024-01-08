package com.anshtya.data.model

import com.anshtya.core.local.database.entity.PopularContentEntity
import com.anshtya.core.network.model.NetworkMediaItem

fun NetworkMediaItem.asPopularContentEntity() = PopularContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = when {
        !title.isNullOrEmpty() -> title!!
        !name.isNullOrEmpty() -> name!!
        else -> ""
    },
    overview = overview ?: ""
)