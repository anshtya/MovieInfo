package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.core.model.library.LibraryItem

@Entity(tableName = "favorite_content")
data class FavoriteContentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.nanoTime()
)

fun FavoriteContentEntity.asModel() = LibraryItem(
    id = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun LibraryItem.asFavoriteContentEntity() = FavoriteContentEntity(
    id = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)