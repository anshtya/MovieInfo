package com.anshtya.movieinfo.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_10_11
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_11_12
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_1_2
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_2_3
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_3_4
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_4_5
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_6_7
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_7_8
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_8_9
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.MIGRATION_9_10
import com.anshtya.movieinfo.core.database.MovieInfoDatabase.Companion.Migration5to6
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MovieInfoDatabaseMigrationTest {
    private val testDbName = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MovieInfoDatabase::class.java,
        listOf(Migration5to6())
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        helper.createDatabase(testDbName, 1).apply {
            execSQL(
                """
                INSERT INTO trending_content (id, image_path, name, overview)
                VALUES (1, 'image.jpg', 'Trending Movie', 'Overview text')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 2, true, MIGRATION_1_2)

        db.query("SELECT * FROM trending_content").use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "Trending Movie",
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        helper.createDatabase(testDbName, 2).apply {
            execSQL(
                """
                INSERT INTO free_content (id, image_path, name, overview)
                VALUES (100, 'free.jpg', 'Free Movie', 'Free overview')
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO popular_content (id, image_path, name, overview)
                VALUES (200, 'popular.jpg', 'Popular Movie', 'Popular overview')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 3, true, MIGRATION_2_3)

        db.query("SELECT * FROM free_content WHERE remote_id = 100").use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals("Free Movie", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        }
        db.query("SELECT * FROM popular_content WHERE remote_id = 200").use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "Popular Movie",
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate3To4() {
        helper.createDatabase(testDbName, 3).apply {
            execSQL(
                """
                INSERT INTO free_content (id, remote_id, image_path, name, overview)
                VALUES (1, 100, 'free.jpg', 'Free Movie', 'Free overview')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 4, true, MIGRATION_3_4)

        db.query("SELECT * FROM free_content").use { cursor ->
            assertEquals(1, cursor.count)
        }
        // Newly created remote key tables should be queryable and empty.
        db.query("SELECT * FROM free_content_remote_key").use { cursor ->
            assertEquals(0, cursor.count)
        }
        db.query("SELECT * FROM popular_content_remote_key").use { cursor ->
            assertEquals(0, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate4To5() {
        helper.createDatabase(testDbName, 4).apply {
            execSQL(
                """
                INSERT INTO trending_content (id, remote_id, image_path, name, overview)
                VALUES (1, 100, 'trending.jpg', 'Trending Movie', 'Trending overview')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 5, true, MIGRATION_4_5)

        db.query("SELECT * FROM trending_content").use { cursor ->
            assertEquals(1, cursor.count)
        }
        db.query("SELECT * FROM entity_last_modified").use { cursor ->
            assertEquals(3, cursor.count)
        }
        db.query(
            "SELECT last_modified FROM entity_last_modified WHERE name = 'trending_content'"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(0L, cursor.getLong(cursor.getColumnIndexOrThrow("last_modified")))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate5To6() {
        helper.createDatabase(testDbName, 5).apply {
            execSQL(
                """
                INSERT INTO trending_content (id, remote_id, image_path, name, overview)
                VALUES (1, 100, 'trending.jpg', 'Trending Movie', 'Trending overview')
                """.trimIndent()
            )
            close()
        }

        // 5 -> 6 is an AutoMigration (deletes the `overview` column from the
        // trending/free/popular content tables), so no explicit Migration is passed.
        val db = helper.runMigrationsAndValidate(testDbName, 6, true)

        db.query("SELECT * FROM trending_content").use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "Trending Movie",
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
            assertEquals(-1, cursor.getColumnIndex("overview"))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate6To7() {
        helper.createDatabase(testDbName, 6).close()

        val db = helper.runMigrationsAndValidate(testDbName, 7, true, MIGRATION_6_7)

        db.query("SELECT * FROM favorite_content").use { cursor ->
            assertEquals(0, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate7To8() {
        helper.createDatabase(testDbName, 7).apply {
            execSQL(
                """
                INSERT INTO favorite_content (id, media_type, image_path, name, created_at)
                VALUES (1, 'movie', 'favorite.jpg', 'Favorite Movie', 1000)
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 8, true, MIGRATION_7_8)

        db.query("SELECT * FROM favorite_content").use { cursor ->
            assertEquals(1, cursor.count)
        }
        db.query("SELECT * FROM watchlist_content").use { cursor ->
            assertEquals(0, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate8To9() {
        helper.createDatabase(testDbName, 8).apply {
            execSQL(
                """
                INSERT INTO favorite_content (id, media_type, image_path, name, created_at)
                VALUES (1, 'movie', 'favorite.jpg', 'Favorite Movie', 1000)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO watchlist_content (id, media_type, image_path, name, created_at)
                VALUES (2, 'tv', 'watchlist.jpg', 'Watchlist Show', 2000)
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 9, true, MIGRATION_8_9)

        db.query("SELECT * FROM favorite_content").use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "Favorite Movie",
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
        }
        db.query("SELECT * FROM watchlist_content").use { cursor ->
            assertEquals(1, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate9To10() {
        helper.createDatabase(testDbName, 9).apply {
            execSQL(
                """
                INSERT INTO favorite_content (id, media_type, image_path, name, created_at)
                VALUES (1, 'movie', 'favorite.jpg', 'Favorite Movie', 1000)
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 10, true, MIGRATION_9_10)

        db.query("SELECT * FROM favorite_content").use { cursor ->
            assertEquals(1, cursor.count)
        }
        db.query("SELECT * FROM account_details").use { cursor ->
            assertEquals(0, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate10To11() {
        helper.createDatabase(testDbName, 10).apply {
            execSQL(
                """
                INSERT INTO favorite_content (id, media_type, image_path, name, created_at)
                VALUES (1, 'movie', 'favorite.jpg', 'Favorite Movie', 1000)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO watchlist_content (id, media_type, image_path, name, created_at)
                VALUES (2, 'tv', 'watchlist.jpg', 'Watchlist Show', 2000)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO account_details (
                    id, gravatar_hash, include_adult, iso_639_1, iso_3166_1,
                    name, tmdb_avatar_path, username
                )
                VALUES (1, 'hash', 0, 'en', 'US', 'Test User', NULL, 'testuser')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 11, true, MIGRATION_10_11)

        db.query(
            "SELECT * FROM favorite_content WHERE id = 1 AND media_type = 'movie'"
        ).use { cursor ->
            assertEquals(1, cursor.count)
        }
        db.query(
            "SELECT * FROM watchlist_content WHERE id = 2 AND media_type = 'tv'"
        ).use { cursor ->
            assertEquals(1, cursor.count)
        }
        db.query("SELECT * FROM account_details").use { cursor ->
            assertEquals(1, cursor.count)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate11To12() {
        helper.createDatabase(testDbName, 11).apply {
            execSQL(
                """
                INSERT INTO favorite_content (id, media_type, image_path, name, created_at)
                VALUES (5, 'tv', 'favorite.jpg', 'Favorite Show', 1000)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO watchlist_content (id, media_type, image_path, name, created_at)
                VALUES (6, 'movie', 'watchlist.jpg', 'Watchlist Movie', 2000)
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 12, true, MIGRATION_11_12)

        db.query(
            "SELECT * FROM favorite_content WHERE media_id = 5 AND media_type = 'tv'"
        ).use { cursor ->
            assertEquals(1, cursor.count)
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "Favorite Show",
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
        }
        db.query(
            "SELECT * FROM watchlist_content WHERE media_id = 6 AND media_type = 'movie'"
        ).use { cursor ->
            assertEquals(1, cursor.count)
        }
    }
}
