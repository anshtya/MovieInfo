package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.sync.util.getEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LibraryTaskWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository
): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val itemId = inputData.getInt(TASK_KEY, 0)

        return when(getTaskType()) {
            LibraryTaskType.ADD_FAVORITE -> addFavorite(itemId)
            LibraryTaskType.REMOVE_FAVORITE -> TODO()
            LibraryTaskType.ADD_TO_WATCHLIST -> TODO()
            LibraryTaskType.REMOVE_FROM_WATCHLIST -> TODO()
        }
    }

    private suspend fun addFavorite(id: Int): Result {
        return Result.success()
    }

    private fun getTaskType() = inputData.getEnum<LibraryTaskType>(TASK_TYPE_KEY)

    companion object {
        const val TASK_KEY = "task_key"
        const val MEDIA_KEY = "media_key"
        const val TASK_TYPE_KEY = "task_type_key"
    }
}