package com.anshtya.data.di

import com.anshtya.data.repository.ContentPreferencesRepository
import com.anshtya.data.repository.ContentPreferencesRepositoryImpl
import com.anshtya.data.repository.ContentRepository
import com.anshtya.data.repository.ContentRepositoryImpl
import com.anshtya.data.repository.SearchRepository
import com.anshtya.data.repository.SearchRepositoryImpl
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.data.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    internal abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository

    @Binds
    internal abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    internal abstract fun bindContentPreferencesRepository(
        contentPreferencesRepositoryImpl: ContentPreferencesRepositoryImpl
    ): ContentPreferencesRepository

    @Binds
    internal abstract fun bindUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ): UserDataRepository
}