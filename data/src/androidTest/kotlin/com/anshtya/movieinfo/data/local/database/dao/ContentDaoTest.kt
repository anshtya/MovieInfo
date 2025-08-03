package com.anshtya.movieinfo.data.local.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anshtya.movieinfo.data.local.database.MovieInfoDatabase
import com.anshtya.movieinfo.data.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.data.local.database.entity.WatchlistContentEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
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
                mediaId = 1,
                mediaType = "movie",
                name = "",
                imagePath = ""
            ),
            FavoriteContentEntity(
                id = 2,
                mediaId = 1,
                mediaType = "tv",
                name = "",
                imagePath = ""
            )
        )
        favoriteContentDao.upsertFavoriteItems(favoriteItems)

        assertEquals(
            listOf(favoriteItems[0]),
            favoriteContentDao.getFavoriteMovies().first()
        )

        assertEquals(
            listOf(favoriteItems[1]),
            favoriteContentDao.getFavoriteTvShows().first()
        )
    }

    @Test
    fun watchlistContentDao_sameId_differentMediaType_inserted_separately() = runTest {
        val watchlistItems = listOf(
            WatchlistContentEntity(
                id = 1,
                mediaId = 1,
                mediaType = "movie",
                name = "",
                imagePath = ""
            ),
            WatchlistContentEntity(
                id = 2,
                mediaId = 1,
                mediaType = "tv",
                name = "",
                imagePath = ""
            )
        )
        watchlistContentDao.upsertWatchlistItems(watchlistItems)

        assertEquals(
            listOf(watchlistItems[0]),
            watchlistContentDao.getMoviesWatchlist().first()
        )

        assertEquals(
            listOf(watchlistItems[1]),
            watchlistContentDao.getTvShowsWatchlist().first()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }
}