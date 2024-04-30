package com.anshtya.data.di

import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.ContentRepository
import com.anshtya.data.repository.impl.AuthRepositoryImpl
import com.anshtya.data.repository.DetailsRepository
import com.anshtya.data.repository.impl.DetailsRepositoryImpl
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.impl.LibraryRepositoryImpl
import com.anshtya.data.repository.SearchRepository
import com.anshtya.data.repository.impl.SearchRepositoryImpl
import com.anshtya.data.repository.UserRepository
import com.anshtya.data.repository.impl.ContentRepositoryImpl
import com.anshtya.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

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