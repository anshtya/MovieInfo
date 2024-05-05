package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.anshtya.core.local.database.entity.WatchlistContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistContentDao {

    @Query("SELECT * FROM watchlist_content WHERE media_type = 'movie' ORDER BY created_at")
    fun getMoviesWatchlist(): Flow<List<WatchlistContentEntity>>

    @Query("SELECT * FROM watchlist_content WHERE media_type = 'tv' ORDER BY created_at")
    fun getTvShowsWatchlist(): Flow<List<WatchlistContentEntity>>

    @Query("SELECT * FROM watchlist_content")
    suspend fun getWatchlistItems(): List<WatchlistContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(watchlistContentEntity: WatchlistContentEntity)

    @Delete
    suspend fun deleteWatchlistItem(watchlistContentEntity: WatchlistContentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_content WHERE id = :mediaId)")
    suspend fun checkWatchlistItemExists(mediaId: Int): Boolean

    @Query("DELETE FROM watchlist_content WHERE id in (:ids)")
    suspend fun deleteItems(ids: List<Int>)

    @Query("DELETE FROM watchlist_content")
    suspend fun deleteAllItems()

    @Upsert
    suspend fun upsertWatchlistItems(items: List<WatchlistContentEntity>)

    @Transaction
    suspend fun syncItems(
        upsertItems: List<WatchlistContentEntity>,
        deleteItems: List<Int>
    ) {
        upsertWatchlistItems(upsertItems)
        deleteItems(deleteItems)
    }
}