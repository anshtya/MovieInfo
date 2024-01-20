package com.anshtya.data.repository.impl

import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.local.database.entity.FavoriteContentEntity
import com.anshtya.core.local.database.entity.asFavoriteContentEntity
import com.anshtya.core.local.database.entity.asModel
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.util.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val favoriteContentDao: FavoriteContentDao,
    private val syncManager: SyncManager
) : LibraryRepository {
    override val favoriteMovies: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteMovies()
            .map { it.map(FavoriteContentEntity::asModel) }

    override val favoriteTvShows: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteTvShows()
            .map { it.map(FavoriteContentEntity::asModel) }

    override suspend fun addOrRemoveFavorites(libraryItem: LibraryItem) {
        val favoriteContentEntity = libraryItem.asFavoriteContentEntity()
        val itemExists = favoriteContentDao.checkFavoriteItemExists(libraryItem.id)

        if (itemExists) {
            favoriteContentDao.deleteFavoriteItem(favoriteContentEntity)

            val libraryTask = LibraryTask.removeFavorite(
                mediaId = favoriteContentEntity.id,
                mediaType = enumValueOf(favoriteContentEntity.mediaType)
            )
            syncManager.scheduleWork(libraryTask)
        } else {
            favoriteContentDao.insertFavoriteItem(favoriteContentEntity)

            val libraryTask = LibraryTask.addFavorite(
                mediaId = favoriteContentEntity.id,
                mediaType = enumValueOf(favoriteContentEntity.mediaType)
            )
            syncManager.scheduleWork(libraryTask)
        }
    }

//    suspend fun addTvShowToFavorites(favoriteItem: FavoriteContentEntity) {
//
//    }
//
//    suspend fun removeTvShowFromFavorites(id: Int) {
//
//    }

}