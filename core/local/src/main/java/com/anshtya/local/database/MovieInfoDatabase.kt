package com.anshtya.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anshtya.local.database.dao.FreeContentDao
import com.anshtya.local.database.dao.FreeContentRemoteKeyDao
import com.anshtya.local.database.dao.PopularContentDao
import com.anshtya.local.database.dao.PopularContentRemoteKeyDao
import com.anshtya.local.database.dao.TrendingContentDao
import com.anshtya.local.database.dao.TrendingContentRemoteKeyDao
import com.anshtya.local.database.entity.FreeContentEntity
import com.anshtya.local.database.entity.FreeContentRemoteKey
import com.anshtya.local.database.entity.PopularContentEntity
import com.anshtya.local.database.entity.PopularContentRemoteKey
import com.anshtya.local.database.entity.TrendingContentEntity
import com.anshtya.local.database.entity.TrendingContentRemoteKey

@Database(
    entities = [
        TrendingContentEntity::class,
        TrendingContentRemoteKey::class,
        PopularContentEntity::class,
        PopularContentRemoteKey::class,
        FreeContentEntity::class,
        FreeContentRemoteKey::class
    ],
    version = 4,
    exportSchema = true
)
abstract class MovieInfoDatabase : RoomDatabase() {
    abstract fun trendingContentDao(): TrendingContentDao
    abstract fun popularContentDao(): PopularContentDao
    abstract fun freeContentDao(): FreeContentDao
    abstract fun trendingContentRemoteKeyDao(): TrendingContentRemoteKeyDao
    abstract fun freeContentRemoteKeyDao(): FreeContentRemoteKeyDao
    abstract fun popularContentRemoteKeyDao(): PopularContentRemoteKeyDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    ALTER TABLE trending_content ADD COLUMN remote_id INTEGER NOT NULL
                    """.trimIndent())
                db.execSQL("""
                    ALTER TABLE trending_content 
                    MODIFY COLUMN id INTEGER AUTOINCREMENT NOT NULL
                    """.trimIndent())
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE temp_fc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent())

                db.execSQL("""
                   INSERT INTO temp_fc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM free_content
                """.trimIndent())

                db.execSQL("DROP TABLE free_content")
                db.execSQL("ALTER TABLE temp_fc RENAME TO free_content")

                db.execSQL("""
                    CREATE TABLE temp_pc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent())

                db.execSQL("""
                   INSERT INTO temp_pc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM popular_content
                   """.trimIndent())

                db.execSQL("DROP TABLE popular_content")
                db.execSQL("ALTER TABLE temp_pc RENAME TO popular_content")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS free_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS popular_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent())
            }
        }
    }
}