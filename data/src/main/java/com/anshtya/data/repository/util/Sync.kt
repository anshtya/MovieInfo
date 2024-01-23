package com.anshtya.data.repository.util

import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.model.library.LibraryTaskType

interface SyncManager {
    fun scheduleLibraryTaskWork(libraryTask: LibraryTask)
}

interface Synchronizer {
    suspend fun addOrRemoveItem(
        id: Int,
        mediaType: String,
        libraryTaskType: LibraryTaskType,
        itemExistsLocally: Boolean
    ): Boolean
}