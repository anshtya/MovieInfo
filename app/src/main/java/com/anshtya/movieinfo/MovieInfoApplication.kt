package com.anshtya.movieinfo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.data.repository.util.SyncManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltAndroidApp
class MovieInfoApplication : Application(), ImageLoaderFactory, Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var syncManager: SyncManager

    private var applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        userDataRepository.userData
            .map { it.isLoggedIn }
            .distinctUntilChanged()
            .onEach {
                if (it) syncManager.scheduleLibrarySyncWork()
            }
            .launchIn(applicationScope)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
        applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }
}