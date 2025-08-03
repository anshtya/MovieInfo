package com.anshtya.movieinfo.data.network.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAccountDetails(
    val avatar: Avatar,
    val id: Int,
    @Json(name = "include_adult") val includeAdult: Boolean,
    @Json(name = "iso_639_1") val iso6391: String,
    @Json(name = "iso_3166_1") val iso31661: String,
    val name: String,
    val username: String
)

@JsonClass(generateAdapter = true)
data class Avatar(
    val gravatar: Gravatar,
    val tmdb: Tmdb
)

@JsonClass(generateAdapter = true)
data class Gravatar(
    val hash: String
)

@JsonClass(generateAdapter = true)
data class Tmdb(
    @Json(name = "avatar_path") val avatarPath: String?
)