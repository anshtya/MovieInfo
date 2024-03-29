package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.sync.util.SYNC_NOTIFICATION_ID
import com.anshtya.sync.util.workNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class LibrarySyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository,
    private val userDataRepository: UserDataRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }

    override suspend fun doWork(): Result {

        val userLoggedIn = userDataRepository.userData.first().isLoggedIn

        return if (userLoggedIn) {
            val syncSuccessful = libraryRepository.syncLibrary()

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