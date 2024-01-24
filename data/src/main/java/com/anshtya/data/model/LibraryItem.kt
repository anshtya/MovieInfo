package com.anshtya.data.model

import com.anshtya.core.local.database.entity.FavoriteContentEntity
import com.anshtya.core.local.database.entity.WatchlistContentEntity
import com.anshtya.core.model.MediaType
import com.anshtya.core.network.model.content.NetworkContentItem

fun NetworkContentItem.asFavoriteMovieEntity() = FavoriteContentEntity(
    id = id,
    name = title!!,
    imagePath = posterPath,
    mediaType = MediaType.MOVIE.name
)

fun NetworkContentItem.asFavoriteTvShowEntity() = FavoriteContentEntity(
    id = id,
    name = name!!,
    imagePath = posterPath,
    mediaType = MediaType.TV.name
)

fun NetworkContentItem.asWatchlistMovieEntity() = WatchlistContentEntity(
    id = id,
    name = title!!,
    imagePath = posterPath,
    mediaType = MediaType.MOVIE.name
)

fun NetworkContentItem.asWatchlistTvShowEntity() = WatchlistContentEntity(
    id = id,
    name = name!!,
    imagePath = posterPath,
    mediaType = MediaType.TV.name
)