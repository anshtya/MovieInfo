package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.anshtya.core.model.library.LibraryItem

@Entity(
    tableName = "watchlist_content",
    primaryKeys = ["id", "media_type"]
)
data class WatchlistContentEntity(
    val id: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.nanoTime()
) {
    fun asModel() = LibraryItem(
        id = id,
        mediaType = mediaType,
        imagePath = imagePath,
        name = name
    )
}

fun LibraryItem.asWatchlistContentEntity() = WatchlistContentEntity(
    id = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)
