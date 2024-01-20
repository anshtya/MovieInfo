package com.anshtya.movieinfo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class MovieInfoApplication : Application(), ImageLoaderFactory, Configuration.Provider {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(
                EntryPoints
                    .get(this, HiltWorkerFactoryEntryPoint::class.java)
                    .workerFactory()
            )
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}