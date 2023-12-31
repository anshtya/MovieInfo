package com.anshtya.core.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_last_modified")
data class EntityLastModified(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long
)
