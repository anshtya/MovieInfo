package com.anshtya.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "free_content_remote_key")
data class FreeContentRemoteKey(
    @PrimaryKey
    val id: Long,
    val nextKey: Int?,
)
