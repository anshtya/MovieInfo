package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.core.model.TrendingItem

@Entity(tableName = "trending_content")
data class TrendingContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    val name: String,
    val overview: String
)

fun TrendingContentEntity.asModel() = TrendingItem(
    id = remoteId,
    imagePath = imagePath,
    name = name,
    overview = overview
)