package com.anshtya.movieinfo.sync.di

import com.anshtya.movieinfo.data.util.SyncScheduler
import com.anshtya.movieinfo.sync.SyncSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SyncSchedulerModule{
    @Binds
    abstract fun provideSyncScheduler(
        syncSchedulerImpl: SyncSchedulerImpl
    ): SyncScheduler
}