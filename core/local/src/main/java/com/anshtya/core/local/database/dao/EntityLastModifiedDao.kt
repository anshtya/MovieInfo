package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.anshtya.core.local.database.entity.EntityLastModified

@Dao
interface EntityLastModifiedDao {
    @Upsert
    suspend fun upsertLastModifiedTime(entity: EntityLastModified)

    @Query("SELECT last_modified FROM entity_last_modified WHERE name = :entityName")
    suspend fun entityLastModified(entityName: String): Long
}