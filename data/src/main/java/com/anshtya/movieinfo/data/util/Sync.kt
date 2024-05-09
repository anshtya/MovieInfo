package com.anshtya.movieinfo.data.util

import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.model.library.LibraryTask

// Interface for class which schedules work
interface SyncManager {
    fun scheduleLibraryTaskWork(libraryTask: LibraryTask)

    fun scheduleLibrarySyncWork()

    fun isWorkNotScheduled(id: Int, itemType: LibraryItemType): Boolean
}

// Interface for class which manages sync between local and remote data source
interface Synchronizer {
    suspend fun addOrRemoveItemSync(
        id: Int,
        mediaType: String,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean

    suspend fun syncFavorites(): Boolean

    suspend fun syncWatchlist(): Boolean
}