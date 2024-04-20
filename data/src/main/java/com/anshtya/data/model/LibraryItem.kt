package com.anshtya.data.model

import com.anshtya.core.local.database.entity.FavoriteContentEntity
import com.anshtya.core.local.database.entity.WatchlistContentEntity
import com.anshtya.core.model.MediaType
import com.anshtya.core.network.model.content.NetworkContentItem

fun NetworkContentItem.asFavoriteContentEntity() = FavoriteContentEntity(
    id = id,
    name = title ?: name ?: "",
    imagePath = posterPath ?: "",
    mediaType = if (title != null) MediaType.MOVIE.name else MediaType.TV.name
)

fun NetworkContentItem.asWatchlistContentEntity() = WatchlistContentEntity(
    id = id,
    name = title ?: name ?: "",
    imagePath = posterPath ?: "",
    mediaType = if (title != null) MediaType.MOVIE.name else MediaType.TV.name
)