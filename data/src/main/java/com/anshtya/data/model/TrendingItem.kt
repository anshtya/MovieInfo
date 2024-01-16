package com.anshtya.data.model

import com.anshtya.core.local.database.entity.TrendingContentEntity
import com.anshtya.core.network.model.NetworkContentItem

fun NetworkContentItem.asTrendingContentEntity() = TrendingContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = title ?: name ?: ""
)