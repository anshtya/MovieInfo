package com.anshtya.core.model.library

import com.anshtya.core.model.MediaType

class LibraryTask private constructor(
    val mediaId: Int,
    val mediaType: MediaType,
    val taskType: LibraryTaskType
) {
    companion object {
        fun addFavorite(mediaId: Int, mediaType: MediaType) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.ADD_FAVORITE)

        fun removeFavorite(mediaId: Int, mediaType: MediaType) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.REMOVE_FAVORITE)

        fun addToWatchList(mediaId: Int, mediaType: MediaType) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.ADD_TO_WATCHLIST)

        fun removeFromWatchList(mediaId: Int, mediaType: MediaType) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.REMOVE_FROM_WATCHLIST)
    }
}

enum class LibraryTaskType {
    ADD_FAVORITE,
    REMOVE_FAVORITE,
    ADD_TO_WATCHLIST,
    REMOVE_FROM_WATCHLIST
}