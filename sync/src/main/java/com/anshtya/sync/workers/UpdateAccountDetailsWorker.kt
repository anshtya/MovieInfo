package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.data.repository.AuthRepository
import com.anshtya.sync.util.SYNC_NOTIFICATION_ID
import com.anshtya.sync.util.workNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateAccountDetailsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workParams: WorkerParameters,
    private val authRepository: AuthRepository
): CoroutineWorker(appContext, workParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }
    override suspend fun doWork(): Result {
        val updateSuccessful = authRepository.updateAccountDetails()

        return if (updateSuccessful) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val UPDATE_ACCOUNT_WORK_NAME = "update_account"
    }
}