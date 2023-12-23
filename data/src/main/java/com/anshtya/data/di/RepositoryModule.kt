package com.anshtya.data.di

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
abstract class RepositoryModule {
    @Binds
    abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ): UserDataRepository
}