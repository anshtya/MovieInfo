package com.anshtya.movieinfo.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccountDetails(
    val avatar: Avatar,
    val id: Int,
    @SerialName("include_adult") val includeAdult: Boolean,
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("iso_3166_1") val iso31661: String,
    val name: String,
    val username: String
)

@Serializable
data class Avatar(
    val gravatar: Gravatar,
    val tmdb: Tmdb
)

@Serializable
data class Gravatar(
    val hash: String
)

@Serializable
data class Tmdb(
    @SerialName("avatar_path") val avatarPath: String?
)