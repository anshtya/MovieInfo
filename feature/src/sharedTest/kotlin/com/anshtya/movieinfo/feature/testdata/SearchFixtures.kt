package com.anshtya.movieinfo.feature.testdata

import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.SearchItem

val testSearchResults: List<SearchItem> = listOf(
    SearchItem(
        id = 1,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.MOVIE.name.lowercase()
    ),
    SearchItem(
        id = 2,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.MOVIE.name.lowercase()
    ),
    SearchItem(
        id = 3,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.MOVIE.name.lowercase()
    ),
    SearchItem(
        id = 4,
        imagePath = "path",
        name = "name",
        mediaType = MediaType.MOVIE.name.lowercase()
    )
)
