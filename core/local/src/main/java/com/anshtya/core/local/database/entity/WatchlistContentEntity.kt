package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.core.model.library.LibraryItem

@Entity(tableName = "watchlist_content")
data class WatchlistContentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.nanoTime()
)

fun WatchlistContentEntity.asModel() = LibraryItem(
    id = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)

fun LibraryItem.asWatchlistContentEntity() = WatchlistContentEntity(
    id = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)
