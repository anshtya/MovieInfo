package com.anshtya.movieinfo.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.sync.util.getEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LibraryTaskWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val itemId = inputData.getInt(TASK_KEY, 0)
        val mediaType = inputData.getEnum<MediaType>(MEDIA_TYPE_KEY)
        val taskType = inputData.getEnum<LibraryItemType>(ITEM_TYPE_KEY)
        val itemExists = inputData.getBoolean(ITEM_EXISTS_KEY, false)

        val syncSuccessful = libraryRepository.executeLibraryTask(
            id = itemId,
            mediaType = mediaType,
            libraryItemType = taskType,
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
        const val MEDIA_TYPE_KEY = "media_type_key"
        const val ITEM_TYPE_KEY = "item_type_key"
        const val ITEM_EXISTS_KEY = "item_exists_key"
    }
}