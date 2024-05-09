package com.anshtya.movieinfo.data.model

import com.anshtya.movieinfo.core.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.local.database.entity.WatchlistContentEntity
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem

fun NetworkContentItem.asFavoriteContentEntity() = FavoriteContentEntity(
    id = id,
    name = title ?: name ?: "",
    imagePath = posterPath ?: "",
    mediaType = if (title != null) {
        MediaType.MOVIE.name.lowercase()
    } else {
        MediaType.TV.name.lowercase()
    }
)

fun NetworkContentItem.asWatchlistContentEntity() = WatchlistContentEntity(
    id = id,
    name = title ?: name ?: "",
    imagePath = posterPath ?: "",
    mediaType = if (title != null) {
        MediaType.MOVIE.name.lowercase()
    } else {
        MediaType.TV.name.lowercase()
    }
)