package com.anshtya.movieinfo.core.network.model.auth

import com.squareup.moshi.Json

data class NetworkAccountDetails(
    val avatar: Avatar,
    val id: Int,
    @Json(name = "include_adult") val includeAdult: Boolean,
    @Json(name = "iso_639_1") val iso6391: String,
    @Json(name = "iso_3166_1") val iso31661: String,
    val name: String,
    val username: String
)

data class Avatar(
    val gravatar: Gravatar,
    val tmdb: Tmdb
)

data class Gravatar(
    val hash: String
)

data class Tmdb(
    @Json(name = "avatar_path") val avatarPath: String?
)