package com.anshtya.core.model

data class AccountDetails(
    val id: Long,
    val name: String,
    val username: String,
    val gravatar: String,
    val avatar: String,
    val includeAdult: Boolean,
)