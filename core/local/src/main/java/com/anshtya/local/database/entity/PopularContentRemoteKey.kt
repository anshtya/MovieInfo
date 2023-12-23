package com.anshtya.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_content_remote_key")
data class PopularContentRemoteKey(
    @PrimaryKey
    val id: Long,
    val nextKey: Int?,
)