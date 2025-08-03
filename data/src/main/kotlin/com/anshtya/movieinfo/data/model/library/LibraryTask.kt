package com.anshtya.movieinfo.data.model.library

import com.anshtya.movieinfo.data.model.MediaType

data class LibraryTask(
    val mediaId: Int,
    val mediaType: MediaType,
    val itemType: LibraryItemType,
    val itemExistLocally: Boolean
) {
    companion object {
        fun favoriteItemTask(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryItemType.FAVORITE, itemExists)

        fun watchlistItemTask(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryTask(mediaId, mediaType, LibraryItemType.WATCHLIST, itemExists)
    }
}