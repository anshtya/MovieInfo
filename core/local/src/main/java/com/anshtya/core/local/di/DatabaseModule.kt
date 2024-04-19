package com.anshtya.core.local.di

import android.content.Context
import androidx.room.Room
import com.anshtya.core.local.database.MovieInfoDatabase
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_1_2
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_2_3
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_3_4
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_4_5
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_6_7
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_7_8
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_8_9
import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.local.database.dao.WatchlistContentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun provideMovieInfoDatabase(
        @ApplicationContext context: Context
    ): MovieInfoDatabase {
        return Room
            .databaseBuilder(context, MovieInfoDatabase::class.java, "movie_info.db")
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteContentDao(
        db: MovieInfoDatabase
    ): FavoriteContentDao {
        return db.favoriteContentDao()
    }

    @Singleton
    @Provides
    fun provideWatchlistContentDao(
        db: MovieInfoDatabase
    ): WatchlistContentDao {
        return db.watchlistContentDao()
    }
}