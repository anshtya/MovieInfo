package com.anshtya.core.model.library

import com.anshtya.core.model.MediaType

class LibraryTask private constructor(
    val mediaId: Int,
    val mediaType: MediaType,
    val taskType: LibraryTaskType,
    val itemExistLocally: Boolean
) {
    companion object {
        fun favoriteItemTask(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.FAVORITES, itemExists)

        fun watchlistItemTask(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.WATCHLIST, itemExists)
    }
}

enum class LibraryTaskType {
    FAVORITES, WATCHLIST
}