package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anshtya.data.repository.LibraryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LibrarySyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}