package com.anshtya.movieinfo.data.workmanager.di

import com.anshtya.movieinfo.data.repository.util.SyncScheduler
import com.anshtya.movieinfo.data.workmanager.SyncSchedulerImpl
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