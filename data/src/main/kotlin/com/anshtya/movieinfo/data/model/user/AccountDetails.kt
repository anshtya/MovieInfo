package com.anshtya.movieinfo.data.model.user

import com.anshtya.movieinfo.core.database.entity.AccountDetailsEntity

data class AccountDetails(
    val avatar: String?,
    val gravatar: String,
    val id: Int,
    val includeAdult: Boolean,
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val username: String,
)

fun AccountDetailsEntity.asModel() = AccountDetails(
    id = id,
    gravatar = gravatarHash,
    includeAdult = includeAdult,
    iso6391 = iso6391,
    iso31661 = iso31661,
    name = name,
    avatar = tmdbAvatarPath,
    username = username
)