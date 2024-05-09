package com.anshtya.movieinfo.core.model.library

class LibraryTask private constructor(
    val mediaId: Int,
    val mediaType: String,
    val itemType: LibraryItemType,
    val itemExistLocally: Boolean
) {
    companion object {
        fun favoriteItemTask(mediaId: Int, mediaType: String, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryItemType.FAVORITE, itemExists)

        fun watchlistItemTask(mediaId: Int, mediaType: String, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryItemType.WATCHLIST, itemExists)
    }
}