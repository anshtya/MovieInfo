package com.anshtya.movieinfo.feature.testdata

import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.library.LibraryItem

val testLibraryItems: List<LibraryItem> = listOf(
    LibraryItem(
        id = 1,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.MOVIE.name.lowercase()
    ),
    LibraryItem(
        id = 2,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.TV.name.lowercase()
    )
)
