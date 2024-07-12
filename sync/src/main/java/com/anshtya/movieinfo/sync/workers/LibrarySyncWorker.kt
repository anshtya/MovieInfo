package com.anshtya.movieinfo.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.sync.util.SYNC_NOTIFICATION_ID
import com.anshtya.movieinfo.sync.util.workNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

@HiltWorker
class LibrarySyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }

    override suspend fun doWork(): Result = coroutineScope {

        val userLoggedIn = authRepository.isLoggedIn.first()

        return@coroutineScope if (userLoggedIn) {
            val syncFavorites = async { libraryRepository.syncFavorites() }
            val syncWatchList = async { libraryRepository.syncWatchlist() }
            val syncSuccessful = awaitAll(syncFavorites, syncWatchList).all { it }

            if (syncSuccessful) {
                Result.success()
            } else {
                Result.retry()
            }
        } else {
            Result.success()
        }
    }

    companion object {
        const val SYNC_LIBRARY_WORK_NAME = "sync_library"
    }
}