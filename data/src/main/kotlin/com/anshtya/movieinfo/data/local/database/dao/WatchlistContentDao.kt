package com.anshtya.movieinfo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.anshtya.movieinfo.data.local.database.entity.WatchlistContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistContentDao {

    @Query("SELECT * FROM watchlist_content WHERE media_type = 'movie' ORDER BY id DESC")
    fun getMoviesWatchlist(): Flow<List<WatchlistContentEntity>>

    @Query("SELECT * FROM watchlist_content WHERE media_type = 'tv' ORDER BY id DESC")
    fun getTvShowsWatchlist(): Flow<List<WatchlistContentEntity>>

    @Query("SELECT * FROM watchlist_content WHERE media_id = :mediaId AND media_type = :mediaType")
    suspend fun getWatchlistItem(mediaId: Int, mediaType: String): WatchlistContentEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlistItem(watchlistContentEntity: WatchlistContentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_content WHERE media_id = :mediaId AND media_type = :mediaType)")
    suspend fun checkWatchlistItemExists(mediaId: Int, mediaType: String): Boolean

    @Query("DELETE FROM watchlist_content WHERE media_id = :mediaId AND media_type = :mediaType ")
    suspend fun deleteWatchlistItem(mediaId: Int, mediaType: String)

    @Query("DELETE FROM watchlist_content")
    suspend fun deleteAllWatchlistItems()

    @Upsert
    suspend fun upsertWatchlistItems(items: List<WatchlistContentEntity>)

    @Transaction
    suspend fun syncWatchlistItems(
        upsertItems: List<WatchlistContentEntity>,
        deleteItems: List<Pair<Int, String>>
    ) {
        upsertWatchlistItems(upsertItems)
        deleteItems.forEach {
            deleteWatchlistItem(mediaId = it.first, mediaType = it.second)
        }
    }
}