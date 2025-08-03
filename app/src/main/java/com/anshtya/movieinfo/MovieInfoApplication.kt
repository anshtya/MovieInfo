package com.anshtya.movieinfo

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MovieInfoApplication : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .apply {
                setWorkerFactory(workerFactory)
                if (BuildConfig.DEBUG) {
                    setMinimumLoggingLevel(android.util.Log.DEBUG)
                } else {
                    setMinimumLoggingLevel(android.util.Log.ERROR)
                }
            }
            .build()

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}