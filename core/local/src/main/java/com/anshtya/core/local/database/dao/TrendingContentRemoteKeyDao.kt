package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.core.local.database.entity.TrendingContentRemoteKey

@Dao
interface TrendingContentRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: List<TrendingContentRemoteKey>)

    @Query("SELECT * FROM trending_content_remote_key WHERE id = :query")
    suspend fun remoteKeyByQuery(query: Long): TrendingContentRemoteKey

    @Query("DELETE FROM trending_content_remote_key")
    suspend fun clearAll()
}