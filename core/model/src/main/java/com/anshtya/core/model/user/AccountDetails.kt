package com.anshtya.core.model.user

data class AccountDetails(
    val id: Int,
    val name: String,
    val username: String,
    val gravatar: String,
    val avatar: String,
    val includeAdult: Boolean,
)