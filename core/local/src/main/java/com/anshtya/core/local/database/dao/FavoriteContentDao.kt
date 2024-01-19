package com.anshtya.core.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.anshtya.core.local.database.entity.FavoriteContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteContentDao {

//    @Query("SELECT * FROM trending_content WHERE media_type = 'movie'")
//    fun getFavoriteMovies(): Flow<List<FavoriteContentEntity>>
//
//    @Query("SELECT * FROM trending_content WHERE media_type = 'tv'")
//    fun getFavoriteTvShows(): Flow<List<FavoriteContentEntity>>
//
//    @Upsert
//    suspend fun insertFavoriteItem(favoriteContentEntity: FavoriteContentEntity)
//
//    @Query("DELETE FROM trending_content WHERE id = :id")
//    suspend fun deleteFavoriteItem(id: Int)
}