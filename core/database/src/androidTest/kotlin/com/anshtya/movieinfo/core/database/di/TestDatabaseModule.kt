package com.anshtya.movieinfo.core.database.di

import android.content.Context
import androidx.room.Room
import com.anshtya.movieinfo.core.database.MovieInfoDatabase
import com.anshtya.movieinfo.core.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.database.dao.WatchlistContentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object TestDatabaseModule {
    @Singleton
    @Provides
    fun provideMovieInfoDatabase(
        @ApplicationContext context: Context
    ): MovieInfoDatabase {
        return Room
            .inMemoryDatabaseBuilder(context, MovieInfoDatabase::class.java)
            .allowMainThreadQueries()
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

    @Singleton
    @Provides
    fun provideAccountDetailsDao(
        db: MovieInfoDatabase
    ): AccountDetailsDao {
        return db.accountDetailsDao()
    }
}
