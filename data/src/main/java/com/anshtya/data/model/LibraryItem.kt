package com.anshtya.data.model

import com.anshtya.core.local.database.entity.FavoriteContentEntity
import com.anshtya.core.model.MediaType
import com.anshtya.core.network.model.content.NetworkContentItem

fun NetworkContentItem.asFavoriteMovieEntity() = FavoriteContentEntity(
    mediaId = id,
    name = title!!,
    imagePath = posterPath,
    mediaType = MediaType.MOVIE.name
)

fun NetworkContentItem.asFavoriteTvShowEntity() = FavoriteContentEntity(
    mediaId = id,
    name = name!!,
    imagePath = posterPath,
    mediaType = MediaType.TV.name
)