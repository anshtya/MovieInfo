package com.anshtya.movieinfo.core.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.anshtya.movieinfo.core.local.database.entity.FavoriteContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteContentDao {

    @Query("SELECT * FROM favorite_content WHERE media_type = 'movie' ORDER BY created_at")
    fun getFavoriteMovies(): Flow<List<FavoriteContentEntity>>

    @Query("SELECT * FROM favorite_content WHERE media_type = 'tv' ORDER BY created_at")
    fun getFavoriteTvShows(): Flow<List<FavoriteContentEntity>>

    @Query("SELECT * FROM favorite_content")
    suspend fun getFavoriteItems(): List<FavoriteContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(favoriteContentEntity: FavoriteContentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_content WHERE id = :mediaId)")
    suspend fun checkFavoriteItemExists(mediaId: Int): Boolean

    @Delete
    suspend fun deleteFavoriteItem(favoriteContentEntity: FavoriteContentEntity)

    @Query("DELETE FROM favorite_content WHERE id in (:ids)")
    suspend fun deleteItems(ids: List<Int>)

    @Query("DELETE FROM favorite_content")
    suspend fun deleteAllItems()

    @Upsert
    suspend fun upsertFavoriteItems(items: List<FavoriteContentEntity>)

    @Transaction
    suspend fun syncItems(
        upsertItems: List<FavoriteContentEntity>,
        deleteItems: List<Int>
    ) {
        upsertFavoriteItems(upsertItems)
        deleteItems(deleteItems)
    }
}