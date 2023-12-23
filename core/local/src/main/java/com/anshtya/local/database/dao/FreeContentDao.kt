package com.anshtya.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.local.database.entity.FreeContentEntity

@Dao
interface FreeContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trendingItems: List<FreeContentEntity>)

    @Query("SELECT * FROM free_content")
    fun pagingSource(): PagingSource<Int, FreeContentEntity>

    @Query("DELETE FROM free_content")
    suspend fun clearAll()
}