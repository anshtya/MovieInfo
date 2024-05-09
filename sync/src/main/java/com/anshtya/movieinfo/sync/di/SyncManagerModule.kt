package com.anshtya.movieinfo.sync.di

import com.anshtya.movieinfo.data.util.SyncManager
import com.anshtya.movieinfo.sync.SyncManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SyncManagerModule{
    @Binds
    abstract fun provideSyncManager(
        syncManagerImpl: SyncManagerImpl
    ): SyncManager
}