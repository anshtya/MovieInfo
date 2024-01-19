package com.anshtya.data.repository.impl

import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.util.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favoriteContentDao: FavoriteContentDao,
    private val syncManager: SyncManager
) : LibraryRepository {
    override val favoriteMovies: Flow<List<LibraryItem>> = flow {  }
//        favoriteContentDao.getFavoriteMovies()
//            .map { it.map(FavoriteContentEntity::asModel) }

    override val favoriteTvShows: Flow<List<LibraryItem>> = flow {  }
//        favoriteContentDao.getFavoriteTvShows()
//            .map { it.map(FavoriteContentEntity::asModel) }

    override suspend fun addOrRemoveFavorites(libraryItem: LibraryItem) {

    }

//    suspend fun addTvShowToFavorites(favoriteItem: FavoriteContentEntity) {
//
//    }
//
//    suspend fun removeTvShowFromFavorites(id: Int) {
//
//    }

}