package com.anshtya.local.di

import android.content.Context
import androidx.room.Room
import com.anshtya.local.database.dao.TrendingContentDao
import com.anshtya.local.database.MovieInfoDatabase
import com.anshtya.local.database.MovieInfoDatabase.Companion.MIGRATION_1_2
import com.anshtya.local.database.MovieInfoDatabase.Companion.MIGRATION_2_3
import com.anshtya.local.database.MovieInfoDatabase.Companion.MIGRATION_3_4
import com.anshtya.local.database.dao.FreeContentDao
import com.anshtya.local.database.dao.FreeContentRemoteKeyDao
import com.anshtya.local.database.dao.PopularContentDao
import com.anshtya.local.database.dao.PopularContentRemoteKeyDao
import com.anshtya.local.database.dao.TrendingContentRemoteKeyDao
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
                MIGRATION_3_4
            )
            .build()
    }

    @Singleton
    @Provides
    internal fun provideTrendingContentDao(
        db: MovieInfoDatabase
    ): TrendingContentDao {
        return db.trendingContentDao()
    }

    @Singleton
    @Provides
    internal fun providePopularContentDao(
        db: MovieInfoDatabase
    ): PopularContentDao {
        return db.popularContentDao()
    }

    @Singleton
    @Provides
    internal fun provideFreeContentDao(
        db: MovieInfoDatabase
    ): FreeContentDao {
        return db.freeContentDao()
    }

    @Singleton
    @Provides
    internal fun provideTrendingContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): TrendingContentRemoteKeyDao {
        return db.trendingContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    internal fun provideFreeContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): FreeContentRemoteKeyDao {
        return db.freeContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    internal fun providePopularContentRemoteKeyDao(
        db: MovieInfoDatabase
    ): PopularContentRemoteKeyDao {
        return db.popularContentRemoteKeyDao()
    }
}