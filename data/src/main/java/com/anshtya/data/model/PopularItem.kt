package com.anshtya.data.model

import com.anshtya.core.local.database.entity.PopularContentEntity
import com.anshtya.core.network.model.NetworkContentItem

fun NetworkContentItem.asPopularContentEntity() = PopularContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = title ?: name ?: ""
)