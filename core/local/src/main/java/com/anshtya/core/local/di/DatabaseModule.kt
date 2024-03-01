package com.anshtya.core.local.di

import android.content.Context
import androidx.room.Room
import com.anshtya.core.local.database.dao.TrendingContentDao
import com.anshtya.core.local.database.MovieInfoDatabase
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_1_2
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_2_3
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_3_4
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_4_5
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_6_7
import com.anshtya.core.local.database.MovieInfoDatabase.Companion.MIGRATION_7_8
import com.anshtya.core.local.database.dao.EntityLastModifiedDao
import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.local.database.dao.FreeContentDao
import com.anshtya.core.local.database.dao.FreeContentRemoteKeyDao
import com.anshtya.core.local.database.dao.PopularContentDao
import com.anshtya.core.local.database.dao.PopularContentRemoteKeyDao
import com.anshtya.core.local.database.dao.TrendingContentRemoteKeyDao
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
                MIGRATION_7_8
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideEntityLastModifiedDao(
        db: MovieInfoDatabase
    ): EntityLastModifiedDao {
        return db.entityLastModifiedDao()
    }

    @Singleton
    @Provides
    fun provideTrendingContentDao(
        db: MovieInfoDatabase
    ): TrendingContentDao {
        return db.trendingContentDao()
    }

    @Singleton
    @Provides
    fun providePopularContentDao(
        db: MovieInfoDatabase
    ): PopularContentDao {
        return db.popularContentDao()
    }

    @Singleton
    @Provides
    fun provideFreeContentDao(
        db: MovieInfoDatabase
    ): FreeContentDao {
        return db.freeContentDao()
    }

    @Singleton
    @Provides
    fun provideTrendingContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): TrendingContentRemoteKeyDao {
        return db.trendingContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    fun provideFreeContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): FreeContentRemoteKeyDao {
        return db.freeContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    fun providePopularContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): PopularContentRemoteKeyDao {
        return db.popularContentRemoteKeyDao()
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