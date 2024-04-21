package com.anshtya.data.paging

import com.anshtya.core.network.model.content.NetworkContentItem
import com.anshtya.core.network.model.content.NetworkContentResponse

internal val mockMovieListResponse = NetworkContentResponse(
    page = 1,
    results = listOf(
        NetworkContentItem(
            id = 1,
            title = "Movie 1",
            name = null,
            posterPath = "Poster 1",
        ),
        NetworkContentItem(
            id = 2,
            title = "Movie 2",
            name = null,
            posterPath = "Poster 2",
        ),
        NetworkContentItem(
            id = 3,
            title = "Movie 3",
            name = null,
            posterPath = "Poster 3",
        ),
        NetworkContentItem(
            id = 4,
            title = "Movie 4",
            name = null,
            posterPath = "Poster 4",
        ),
    ),
    totalResults = 4,
    totalPages = 1
)