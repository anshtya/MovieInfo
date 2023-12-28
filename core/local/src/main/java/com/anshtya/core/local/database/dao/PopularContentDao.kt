package com.anshtya.core.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.core.local.database.entity.PopularContentEntity

@Dao
interface PopularContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trendingItems: List<PopularContentEntity>)

    @Query("SELECT * FROM popular_content")
    fun pagingSource(): PagingSource<Int, PopularContentEntity>

    @Query("DELETE FROM popular_content")
    suspend fun clearAll()
}