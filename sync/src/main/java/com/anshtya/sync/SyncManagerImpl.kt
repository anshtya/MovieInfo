package com.anshtya.sync

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.data.repository.util.SyncManager
import com.anshtya.sync.util.putEnum
import com.anshtya.sync.workers.LibrarySyncWorker
import com.anshtya.sync.workers.LibraryTaskWorker
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.ITEM_EXISTS_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.MEDIA_TYPE_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.TASK_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.TASK_TYPE_KEY
import javax.inject.Inject

internal class SyncManagerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncManager {
    override fun scheduleLibraryTaskWork(libraryTask: LibraryTask) {
        val libraryTaskWorkRequest = OneTimeWorkRequestBuilder<LibraryTaskWorker>()
            .setConstraints(getWorkConstraints())
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(generateInputData(libraryTask))
            .build()

        workManager.enqueueUniqueWork(
            generateWorkerName(libraryTask),
            ExistingWorkPolicy.REPLACE,
            libraryTaskWorkRequest
        )
    }

    override fun scheduleLibrarySyncWork() {
        val librarySyncWorkRequest = OneTimeWorkRequestBuilder<LibrarySyncWorker>()
            .setConstraints(getWorkConstraints())
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            LibrarySyncWorker.SYNC_LIBRARY_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            librarySyncWorkRequest
        )
    }

    private fun generateWorkerName(libraryTask: LibraryTask): String {
        return when (libraryTask.taskType) {
            LibraryTaskType.FAVORITES ->
                "fav_${libraryTask.itemExistLocally}_${libraryTask.mediaId}"

            LibraryTaskType.WATCHLIST ->
                "wl_${libraryTask.itemExistLocally}_${libraryTask.mediaId}"
        }
    }

    private fun generateInputData(libraryTask: LibraryTask) = Data.Builder()
        .putInt(TASK_KEY, libraryTask.mediaId)
        .putEnum(MEDIA_TYPE_KEY, libraryTask.mediaType)
        .putEnum(TASK_TYPE_KEY, libraryTask.taskType)
        .putBoolean(ITEM_EXISTS_KEY, libraryTask.itemExistLocally)
        .build()

    private fun getWorkConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}