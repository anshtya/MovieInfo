package com.anshtya.movieinfo.core.local.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anshtya.movieinfo.core.local.database.MovieInfoDatabase
import com.anshtya.movieinfo.core.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.local.database.entity.WatchlistContentEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ContentDaoTest {
    private lateinit var db: MovieInfoDatabase
    private lateinit var favoriteContentDao: FavoriteContentDao
    private lateinit var watchlistContentDao: WatchlistContentDao

    @Before
    fun setUp() {
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            testContext, MovieInfoDatabase::class.java
        ).build()
        favoriteContentDao = db.favoriteContentDao()
        watchlistContentDao = db.watchlistContentDao()
    }

    @Test
    fun favoriteContentDao_sameId_differentMediaType_inserted_separately() = runTest {
        val favoriteItems = listOf(
            FavoriteContentEntity(
                id = 1,
                mediaType = "movie",
                name = "",
                imagePath = "",
                createdAt = 0L
            ),
            FavoriteContentEntity(
                id = 1,
                mediaType = "tv",
                name = "",
                imagePath = "",
                createdAt = 0L
            ),
        )
        favoriteContentDao.upsertFavoriteItems(favoriteItems)
        assertEquals(
            favoriteItems,
            favoriteContentDao.getFavoriteItems()
        )
    }

    @Test
    fun watchlistContentDao_sameId_differentMediaType_inserted_separately() = runTest {
        val watchlistItems = listOf(
            WatchlistContentEntity(
                id = 1,
                mediaType = "movie",
                name = "",
                imagePath = "",
                createdAt = 0L
            ),
            WatchlistContentEntity(
                id = 1,
                mediaType = "tv",
                name = "",
                imagePath = "",
                createdAt = 0L
            ),
        )
        watchlistContentDao.upsertWatchlistItems(watchlistItems)
        assertEquals(
            watchlistItems,
            watchlistContentDao.getWatchlistItems()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }
}