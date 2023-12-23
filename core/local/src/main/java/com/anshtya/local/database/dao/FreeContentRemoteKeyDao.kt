package com.anshtya.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.local.database.entity.FreeContentRemoteKey

@Dao
interface FreeContentRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: List<FreeContentRemoteKey>)

    @Query("SELECT * FROM free_content_remote_key WHERE id = :query")
    suspend fun remoteKeyByQuery(query: Long): FreeContentRemoteKey

    @Query("DELETE FROM free_content_remote_key")
    suspend fun clearAll()
}