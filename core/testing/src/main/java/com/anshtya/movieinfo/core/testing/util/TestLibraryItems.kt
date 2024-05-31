package com.anshtya.movieinfo.core.testing.util

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
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 3,
        imagePath = "path",
        name = "name",
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 4,
        imagePath = "path",
        name = "name",
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 5,
        imagePath = "path",
        name = "name",
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 6,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    ),
    LibraryItem(
        id = 7,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    ),
    LibraryItem(
        id = 8,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    ),
    LibraryItem(
        id = 9,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    ),
    LibraryItem(
        id = 10,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    )
)