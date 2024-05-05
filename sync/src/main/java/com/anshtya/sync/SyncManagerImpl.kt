package com.anshtya.sync

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryItemType
import com.anshtya.data.util.SyncManager
import com.anshtya.sync.util.FAVORITES_TAG
import com.anshtya.sync.util.WATCHLIST_TAG
import com.anshtya.sync.util.putEnum
import com.anshtya.sync.workers.LibrarySyncWorker
import com.anshtya.sync.workers.LibraryTaskWorker
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.ITEM_EXISTS_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.MEDIA_TYPE_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.TASK_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.ITEM_TYPE_KEY
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class SyncManagerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncManager {
    override fun scheduleLibraryTaskWork(libraryTask: LibraryTask) {
        val libraryTaskWorkRequest = OneTimeWorkRequestBuilder<LibraryTaskWorker>()
            .setConstraints(getWorkConstraints())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10L,
                TimeUnit.SECONDS
            )
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

    override fun isWorkNotScheduled(id: Int, itemType: LibraryItemType): Boolean {
        var workInfoFound = false
        val workTag = getWorkTag(itemType)
        val shouldUpsert = workManager.getWorkInfosForUniqueWork("${workTag}_${id}")
            .get()
            .any {
                when (it.state) {
                    WorkInfo.State.ENQUEUED,
                    WorkInfo.State.RUNNING,
                    WorkInfo.State.BLOCKED -> {
                        workInfoFound = true
                        false
                    }

                    else -> {
                        workInfoFound = true
                        true
                    }
                }
            }

        return if (workInfoFound) {
            shouldUpsert
        } else {
            true
        }
    }

    private fun generateWorkerName(libraryTask: LibraryTask): String {
        return when (libraryTask.itemType) {
            LibraryItemType.FAVORITE -> "${FAVORITES_TAG}_${libraryTask.mediaId}"
            LibraryItemType.WATCHLIST -> "${WATCHLIST_TAG}_${libraryTask.mediaId}"
        }
    }

    private fun generateInputData(libraryTask: LibraryTask) = Data.Builder()
        .putInt(TASK_KEY, libraryTask.mediaId)
        .putString(MEDIA_TYPE_KEY, libraryTask.mediaType)
        .putEnum(ITEM_TYPE_KEY, libraryTask.itemType)
        .putBoolean(ITEM_EXISTS_KEY, libraryTask.itemExistLocally)
        .build()

    private fun getWorkTag(libraryItemType: LibraryItemType): String {
        return when (libraryItemType) {
            LibraryItemType.FAVORITE -> FAVORITES_TAG
            LibraryItemType.WATCHLIST -> WATCHLIST_TAG
        }
    }

    private fun getWorkConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}