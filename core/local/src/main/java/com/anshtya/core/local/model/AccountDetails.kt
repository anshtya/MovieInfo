package com.anshtya.core.local.model

import com.anshtya.core.local.proto.AccountDetailsProto
import com.anshtya.core.model.AccountDetails

fun AccountDetailsProto.asModel() = AccountDetails(
    id = id,
    name = name,
    username = username,
    gravatar = gravatar,
    avatar = avatar,
    includeAdult = includeAdult
)