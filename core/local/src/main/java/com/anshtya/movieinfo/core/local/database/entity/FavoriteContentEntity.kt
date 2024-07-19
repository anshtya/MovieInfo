package com.anshtya.movieinfo.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.anshtya.movieinfo.core.model.library.LibraryItem

@Entity(
    tableName = "favorite_content",
    indices = [Index(value = ["media_id", "media_type"], unique = true)]
)
data class FavoriteContentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "media_id") val mediaId: Int,
    @ColumnInfo(name = "media_type") val mediaType: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    val name: String
) {
    fun asLibraryItem() = LibraryItem(
        id = mediaId,
        mediaType = mediaType,
        imagePath = imagePath,
        name = name
    )
}

fun LibraryItem.asFavoriteContentEntity() = FavoriteContentEntity(
    mediaId = id,
    mediaType = mediaType,
    imagePath = imagePath,
    name = name
)