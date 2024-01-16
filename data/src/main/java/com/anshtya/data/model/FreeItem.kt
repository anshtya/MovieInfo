package com.anshtya.data.model

import com.anshtya.core.local.database.entity.FreeContentEntity
import com.anshtya.core.network.model.content.NetworkContentItem

fun NetworkContentItem.asFreeContentEntity() = FreeContentEntity(
    remoteId = id,
    imagePath = posterPath,
    name = title ?: name ?: ""
)