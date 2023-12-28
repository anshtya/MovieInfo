package com.anshtya.core.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.core.local.database.entity.TrendingContentEntity

@Dao
interface TrendingContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trendingItems: List<TrendingContentEntity>)

    @Query("SELECT * FROM trending_content")
    fun pagingSource(): PagingSource<Int, TrendingContentEntity>

    @Query("DELETE FROM trending_content")
    suspend fun clearAll()
}