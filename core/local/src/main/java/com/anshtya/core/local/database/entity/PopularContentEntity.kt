package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.core.model.MediaItem

@Entity(tableName = "popular_content")
data class PopularContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    val name: String
)

fun PopularContentEntity.asModel() = MediaItem(
    mediaId = remoteId,
    imagePath = imagePath,
    name = name
)
