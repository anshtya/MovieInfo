package com.anshtya.data.util

import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryItemType

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