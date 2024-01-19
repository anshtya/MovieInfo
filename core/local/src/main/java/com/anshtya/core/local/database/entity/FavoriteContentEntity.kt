package com.anshtya.core.local.database.entity

import com.anshtya.core.model.library.LibraryItem

data class FavoriteContentEntity(
    val id: Int = 0,
    val mediaId: Int,
    val mediaType: String,
    val imagePath: String,
    val name: String
)

fun FavoriteContentEntity.asModel() = LibraryItem(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun LibraryItem.asFavoriteContentEntity() = FavoriteContentEntity(
    mediaId = mediaId,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)