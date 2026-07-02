package com.anshtya.movieinfo.data.model.library

import com.anshtya.movieinfo.core.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.database.entity.WatchlistContentEntity

data class LibraryItem(
    val id: Int,
    val imagePath: String,
    val name: String,
    val mediaType: String
)

fun FavoriteContentEntity.asLibraryItem() = LibraryItem(
    id = mediaId,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun WatchlistContentEntity.asLibraryItem() = LibraryItem(
    id = mediaId,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun LibraryItem.asFavoriteContentEntity() = FavoriteContentEntity(
    mediaId = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun LibraryItem.asWatchlistContentEntity() = WatchlistContentEntity(
    mediaId = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)