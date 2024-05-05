package com.anshtya.core.model.library

class LibraryTask private constructor(
    val mediaId: Int,
    val mediaType: String,
    val taskType: LibraryTaskType,
    val itemExistLocally: Boolean
) {
    companion object {
        fun favoriteItemTask(mediaId: Int, mediaType: String, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.FAVORITE, itemExists)

        fun watchlistItemTask(mediaId: Int, mediaType: String, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryTaskType.WATCHLIST, itemExists)
    }
}

enum class LibraryTaskType {
    FAVORITE, WATCHLIST
}