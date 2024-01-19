package com.anshtya.data.repository.util

import com.anshtya.core.model.library.LibraryTask

interface SyncManager {
    fun scheduleWork(libraryTask: LibraryTask)

}