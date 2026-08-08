package com.anshtya.movieinfo.data.util

import com.anshtya.movieinfo.core.database.entity.AccountDetailsEntity
import com.anshtya.movieinfo.core.network.model.auth.NetworkAccountDetails

fun NetworkAccountDetails.asEntity() = AccountDetailsEntity(
    id = id,
    name = name,
    username = username,
    includeAdult = includeAdult,
    iso6391 = iso6391,
    iso31661 = iso31661,
    gravatarHash = avatar.gravatar.hash,
    tmdbAvatarPath = avatar.tmdb.avatarPath
)