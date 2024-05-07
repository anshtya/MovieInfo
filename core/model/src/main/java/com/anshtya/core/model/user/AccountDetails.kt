package com.anshtya.core.model.user

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