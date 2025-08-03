package com.anshtya.movieinfo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.anshtya.movieinfo.data.local.database.entity.FavoriteContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteContentDao {

    @Query("SELECT * FROM favorite_content WHERE media_type = 'movie' ORDER BY id DESC")
    fun getFavoriteMovies(): Flow<List<FavoriteContentEntity>>

    @Query("SELECT * FROM favorite_content WHERE media_type = 'tv' ORDER BY id DESC")
    fun getFavoriteTvShows(): Flow<List<FavoriteContentEntity>>

    @Query("SELECT * FROM favorite_content WHERE media_id = :mediaId AND media_type = :mediaType")
    suspend fun getFavoriteItem(mediaId: Int, mediaType: String): FavoriteContentEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteItem(favoriteContentEntity: FavoriteContentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_content WHERE media_id = :mediaId AND media_type = :mediaType)")
    suspend fun checkFavoriteItemExists(mediaId: Int, mediaType: String): Boolean

    @Query("DELETE FROM favorite_content WHERE media_id = :mediaId AND media_type = :mediaType ")
    suspend fun deleteFavoriteItem(mediaId: Int, mediaType: String)

    @Query("DELETE FROM favorite_content")
    suspend fun deleteAllFavoriteItems()

    @Upsert
    suspend fun upsertFavoriteItems(items: List<FavoriteContentEntity>)

    @Transaction
    suspend fun syncFavoriteItems(
        upsertItems: List<FavoriteContentEntity>,
        deleteItems: List<Pair<Int, String>>
    ) {
        upsertFavoriteItems(upsertItems)
        deleteItems.forEach {
            deleteFavoriteItem(mediaId = it.first, mediaType = it.second)
        }
    }
}