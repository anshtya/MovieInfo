package com.anshtya.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_content_remote_key")
data class TrendingContentRemoteKey(
    @PrimaryKey
    val id: Long,
    val nextKey: Int?,
)