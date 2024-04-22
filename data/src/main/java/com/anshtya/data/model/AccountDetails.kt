package com.anshtya.data.model

import com.anshtya.core.local.database.entity.AccountDetailsEntity
import com.anshtya.core.network.model.auth.NetworkAccountDetails

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