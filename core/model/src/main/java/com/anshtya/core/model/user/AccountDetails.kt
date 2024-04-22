package com.anshtya.core.model.user

data class AccountDetails(
    val id: Int,
    val gravatar: String,
    val includeAdult: Boolean,
    val iso6391: String,
    val iso31661: String,
    val name: String,
    val username: String,
    val avatar: String?,
)