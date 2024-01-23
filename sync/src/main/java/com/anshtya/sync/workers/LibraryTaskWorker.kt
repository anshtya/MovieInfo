package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.sync.util.SYNC_NOTIFICATION_ID
import com.anshtya.sync.util.getEnum
import com.anshtya.sync.util.workNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LibraryTaskWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }

    override suspend fun doWork(): Result {
        val itemId = inputData.getInt(TASK_KEY, 0)
        val mediaType = inputData.getEnum<MediaType>(MEDIA_TYPE_KEY).name.lowercase()
        val taskType = inputData.getEnum<LibraryTaskType>(TASK_TYPE_KEY)
        val itemExists = inputData.getBoolean(ITEM_EXISTS_KEY, false)

        val syncSuccessful = libraryRepository.addOrRemoveItem(
            id = itemId,
            mediaType = mediaType,
            libraryTaskType = taskType,
            itemExistsLocally = itemExists
        )

        return if (syncSuccessful) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val TASK_KEY = "task_key"
        const val MEDIA_TYPE_KEY = "media_key"
        const val TASK_TYPE_KEY = "task_type_key"
        const val ITEM_EXISTS_KEY = "item_exists_key"
    }
}