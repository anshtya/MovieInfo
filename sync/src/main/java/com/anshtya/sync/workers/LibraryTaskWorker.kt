package com.anshtya.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.core.network.model.library.FavoriteRequest
import com.anshtya.core.network.model.library.WatchlistRequest
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.sync.util.SYNC_NOTIFICATION_ID
import com.anshtya.sync.util.getEnum
import com.anshtya.sync.util.workNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

@HiltWorker
class LibraryTaskWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tmdbApi: TmdbApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
): CoroutineWorker(appContext, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }

    override suspend fun doWork(): Result {
        val itemId = inputData.getInt(TASK_KEY, 0)
        val mediaType = inputData.getEnum<MediaType>(MEDIA_TYPE_KEY).name.lowercase()
        val accountId = userPreferencesDataStore.userData.first().accountDetails.id

        return when(getTaskType()) {
            LibraryTaskType.ADD_FAVORITE -> addFavorite(itemId, mediaType, accountId)
            LibraryTaskType.REMOVE_FAVORITE -> removeFavorite(itemId, mediaType, accountId)
            LibraryTaskType.ADD_TO_WATCHLIST -> addToWatchlist(itemId, mediaType, accountId)
            LibraryTaskType.REMOVE_FROM_WATCHLIST ->
                removeFromWatchlist(itemId, mediaType, accountId)
        }
    }

    private suspend fun addFavorite(id: Int, mediaType: String, accountId: Int): Result {
        val favoriteRequest = FavoriteRequest(
            mediaType = mediaType,
            mediaId = id,
            favorite = true
        )

        return try {
            tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun removeFavorite(id: Int, mediaType: String, accountId: Int): Result {
        val favoriteRequest = FavoriteRequest(
            mediaType = mediaType,
            mediaId = id,
            favorite = false
        )

        return try {
            tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun addToWatchlist(id: Int, mediaType: String, accountId: Int): Result {
        val watchlistRequest = WatchlistRequest(
            mediaType = mediaType,
            mediaId = id,
            watchlist = true
        )

        return try {
            tmdbApi.addOrRemoveFromWatchlist(accountId, watchlistRequest)
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun removeFromWatchlist(id: Int, mediaType: String, accountId: Int): Result {
        val watchlistRequest = WatchlistRequest(
            mediaType = mediaType,
            mediaId = id,
            watchlist = false
        )

        return try {
            tmdbApi.addOrRemoveFromWatchlist(accountId, watchlistRequest)
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private fun getTaskType() = inputData.getEnum<LibraryTaskType>(TASK_TYPE_KEY)

    companion object {
        const val TASK_KEY = "task_key"
        const val MEDIA_TYPE_KEY = "media_key"
        const val TASK_TYPE_KEY = "task_type_key"
    }
}