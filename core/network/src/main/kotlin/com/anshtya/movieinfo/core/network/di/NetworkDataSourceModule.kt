package com.anshtya.movieinfo.core.network.di

import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkDataSourceModule {
    @Binds
    abstract fun bindTmdbNetworkDataSource(
        tmdbNetworkDataSourceImpl: TmdbNetworkDataSourceImpl
    ): TmdbNetworkDataSource
}
