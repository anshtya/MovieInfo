package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.core.local.database.entity.PopularContentRemoteKey

@Dao
interface PopularContentRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: List<PopularContentRemoteKey>)

    @Query("SELECT * FROM popular_content_remote_key WHERE id = :query")
    suspend fun remoteKeyByQuery(query: Int): PopularContentRemoteKey

    @Query("DELETE FROM popular_content_remote_key")
    suspend fun clearAll()
}