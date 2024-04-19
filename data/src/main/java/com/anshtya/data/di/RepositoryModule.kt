package com.anshtya.data.di

import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.impl.AuthRepositoryImpl
import com.anshtya.data.repository.MovieRepository
import com.anshtya.data.repository.impl.MovieRepositoryImpl
import com.anshtya.data.repository.DetailsRepository
import com.anshtya.data.repository.impl.DetailsRepositoryImpl
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.impl.LibraryRepositoryImpl
import com.anshtya.data.repository.SearchRepository
import com.anshtya.data.repository.impl.SearchRepositoryImpl
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.data.repository.impl.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ): UserDataRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailsRepositoryImpl
    ): DetailsRepository

    @Binds
    abstract fun bindLibraryRepository(
        libraryRepositoryImpl: LibraryRepositoryImpl
    ): LibraryRepository
}