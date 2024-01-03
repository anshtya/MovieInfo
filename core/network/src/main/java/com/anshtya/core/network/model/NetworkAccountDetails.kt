package com.anshtya.core.network.model

import com.anshtya.core.model.AccountDetails
import com.squareup.moshi.Json

data class NetworkAccountDetails(
    val avatar: Avatar,
    val id: Long,
    @field:Json(name = "include_adult") val includeAdult: Boolean,
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
    @field:Json(name = "avatar_path") val avatarPath: String?
)

fun NetworkAccountDetails.asModel() = AccountDetails(
    id = id,
    name = name,
    username = username,
    includeAdult = includeAdult,
    gravatar = avatar.gravatar.hash,
    avatar = avatar.tmdb.avatarPath ?: ""
)