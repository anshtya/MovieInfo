package com.anshtya.movieinfo.core.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anshtya.movieinfo.core.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.core.local.database.entity.AccountDetailsEntity
import com.anshtya.movieinfo.core.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.local.database.entity.WatchlistContentEntity

@Database(
    entities = [
        FavoriteContentEntity::class,
        WatchlistContentEntity::class,
        AccountDetailsEntity::class
    ],
    version = 12,
    autoMigrations = [
        AutoMigration(from = 5, to = 6, spec = MovieInfoDatabase.Companion.Migration5to6::class)
    ],
    exportSchema = true
)
abstract class MovieInfoDatabase : RoomDatabase() {
    abstract fun favoriteContentDao(): FavoriteContentDao

    abstract fun watchlistContentDao(): WatchlistContentDao

    abstract fun accountDetailsDao(): AccountDetailsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE trending_content ADD COLUMN remote_id INTEGER NOT NULL
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    ALTER TABLE trending_content 
                    MODIFY COLUMN id INTEGER AUTOINCREMENT NOT NULL
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE temp_fc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                   INSERT INTO temp_fc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM free_content
                """.trimIndent()
                )

                db.execSQL("DROP TABLE free_content")
                db.execSQL("ALTER TABLE temp_fc RENAME TO free_content")

                db.execSQL(
                    """
                    CREATE TABLE temp_pc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                   INSERT INTO temp_pc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM popular_content
                   """.trimIndent()
                )

                db.execSQL("DROP TABLE popular_content")
                db.execSQL("ALTER TABLE temp_pc RENAME TO popular_content")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS free_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS popular_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS entity_last_modified (
                    name TEXT PRIMARY KEY NOT NULL, 
                    last_modified INTEGER NOT NULL)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO entity_last_modified VALUES
                    ('trending_content', 0),
                    ('free_content', 0),
                    ('popular_content', 0)
                """.trimIndent()
                )
            }
        }

        @DeleteColumn.Entries(
            DeleteColumn("trending_content", "overview"),
            DeleteColumn("free_content", "overview"),
            DeleteColumn("popular_content", "overview")
        )
        class Migration5to6 : AutoMigrationSpec

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE favorite_content (
                    id INTEGER PRIMARY KEY NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE watchlist_content (
                    id INTEGER PRIMARY KEY NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE entity_last_modified")
                db.execSQL("DROP TABLE free_content")
                db.execSQL("DROP TABLE free_content_remote_key")
                db.execSQL("DROP TABLE popular_content")
                db.execSQL("DROP TABLE popular_content_remote_key")
                db.execSQL("DROP TABLE trending_content")
                db.execSQL("DROP TABLE trending_content_remote_key")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE account_details (
                    id INTEGER PRIMARY KEY NOT NULL,
                    gravatar_hash TEXT NOT NULL,
                    include_adult INTEGER NOT NULL,
                    iso_639_1 TEXT NOT NULL, 
                    iso_3166_1 TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    tmdb_avatar_path TEXT NULL,
                    username TEXT NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE fc (
                    id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    PRIMARY KEY(id,media_type))
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO fc SELECT * FROM favorite_content   
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE favorite_content")
                db.execSQL("ALTER TABLE fc RENAME TO favorite_content")

                db.execSQL(
                    """
                    CREATE TABLE wc (
                    id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    PRIMARY KEY(id,media_type))
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO wc SELECT * FROM watchlist_content   
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE watchlist_content")
                db.execSQL("ALTER TABLE wc RENAME TO watchlist_content")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE favorite_content_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    media_id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO favorite_content_temp (media_id, media_type, image_path, name)
                    SELECT id, media_type, image_path, name FROM favorite_content
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE favorite_content")
                db.execSQL("ALTER TABLE favorite_content_temp RENAME TO favorite_content")
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_favorite_content_media_id_media_type 
                    ON favorite_content (media_id, media_type)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE TABLE watchlist_content_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    media_id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO watchlist_content_temp (media_id, media_type, image_path, name)
                    SELECT id, media_type, image_path, name FROM watchlist_content
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE watchlist_content")
                db.execSQL("ALTER TABLE watchlist_content_temp RENAME TO watchlist_content")
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_watchlist_content_media_id_media_type 
                    ON watchlist_content (media_id, media_type)
                    """.trimIndent()
                )
            }
        }
    }
}