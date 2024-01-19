package com.anshtya.sync

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.data.repository.util.SyncManager
import com.anshtya.sync.util.putEnum
import com.anshtya.sync.workers.LibraryTaskWorker
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.MEDIA_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.TASK_KEY
import com.anshtya.sync.workers.LibraryTaskWorker.Companion.TASK_TYPE_KEY
import javax.inject.Inject

internal class SyncManagerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncManager {
    override fun scheduleWork(libraryTask: LibraryTask) {
        val libraryTaskWorker = OneTimeWorkRequestBuilder<LibraryTaskWorker>()
            .setConstraints(getInputConstraints())
            .setInputData(generateInputData(libraryTask))
            .build()

        workManager.enqueueUniqueWork(
            generateWorkerName(libraryTask),
            ExistingWorkPolicy.KEEP,
            libraryTaskWorker
        )
    }

    private fun generateWorkerName(libraryTask: LibraryTask): String {
        return when (libraryTask.taskType) {
            LibraryTaskType.ADD_FAVORITE -> "add_fav_${libraryTask.id}"
            LibraryTaskType.REMOVE_FAVORITE -> "rem_fav_${libraryTask.id}"
            LibraryTaskType.ADD_TO_WATCHLIST -> "add_wl_${libraryTask.id}"
            LibraryTaskType.REMOVE_FROM_WATCHLIST -> "rem_wl_${libraryTask.id}"
        }
    }

    private fun generateInputData(libraryTask: LibraryTask) = Data.Builder()
        .putInt(TASK_KEY, libraryTask.id)
        .putEnum(MEDIA_KEY, libraryTask.mediaType)
        .putEnum(TASK_TYPE_KEY, libraryTask.taskType)
        .build()

    private fun getInputConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}