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
object DatabaseModule {
    @Singleton
    @Provides
    fun provideMovieInfoDatabase(@ApplicationContext context: Context): MovieInfoDatabase {
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
    fun provideTrendingContentDao(db: MovieInfoDatabase): TrendingContentDao {
        return db.trendingContentDao()
    }

    @Singleton
    @Provides
    fun providePopularContentDao(db: MovieInfoDatabase): PopularContentDao {
        return db.popularContentDao()
    }

    @Singleton
    @Provides
    fun provideFreeContentDao(db: MovieInfoDatabase): FreeContentDao {
        return db.freeContentDao()
    }

    @Singleton
    @Provides
    fun provideTrendingContentRemoteKeyDao(db: MovieInfoDatabase): TrendingContentRemoteKeyDao {
        return db.trendingContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    fun provideFreeContentRemoteKeyDao(db: MovieInfoDatabase): FreeContentRemoteKeyDao {
        return db.freeContentRemoteKeyDao()
    }

    @Singleton
    @Provides
    fun providePopularContentRemoteKeyDao(db: MovieInfoDatabase): PopularContentRemoteKeyDao {
        return db.popularContentRemoteKeyDao()
    }
}