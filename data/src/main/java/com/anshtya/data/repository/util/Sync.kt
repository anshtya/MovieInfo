package com.anshtya.data.repository.util

import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryTaskType

interface SyncManager {
    fun scheduleLibraryTaskWork(libraryTask: LibraryTask)

    fun scheduleLibrarySyncWork()

    fun scheduleAccountDetailsUpdateWork()
}

interface Synchronizer {
    suspend fun addOrRemoveItemSync(
        id: Int,
        mediaType: String,
        libraryTaskType: LibraryTaskType,
        itemExistsLocally: Boolean
    ): Boolean = true

    suspend fun syncLibrary(): Boolean = true

    suspend fun updateAccountDetails(): Boolean = true
}