package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.anshtya.core.local.database.entity.EntityLastModified

@Dao
interface EntityLastModifiedDao {
    @Update
    suspend fun updateLastModifiedTime(entity: EntityLastModified)

    @Query("SELECT last_modified FROM entity_last_modified WHERE name = :entityName")
    suspend fun entityLastModified(entityName: String): Long
}