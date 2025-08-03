package com.anshtya.movieinfo.core.data.testdoubles

import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem

internal val tvMediaType = MediaType.TV.name.lowercase()
internal val movieMediaType = MediaType.MOVIE.name.lowercase()

val testLibraryItems: List<LibraryItem> = listOf(
    LibraryItem(
        id = 1,
        imagePath = "path",
        name = "name",
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 2,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    )
)