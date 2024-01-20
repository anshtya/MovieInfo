package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.core.local.database.entity.FavoriteContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteContentDao {

    @Query("SELECT * FROM favorite_content WHERE media_type = 'MOVIE' ORDER BY created_at")
    fun getFavoriteMovies(): Flow<List<FavoriteContentEntity>>

    @Query("SELECT * FROM favorite_content WHERE media_type = 'TV' ORDER BY created_at")
    fun getFavoriteTvShows(): Flow<List<FavoriteContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(favoriteContentEntity: FavoriteContentEntity)

    @Delete
    suspend fun deleteFavoriteItem(favoriteContentEntity: FavoriteContentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_content WHERE id = :mediaId)")
    suspend fun checkFavoriteItemExists(mediaId: Int): Boolean
}