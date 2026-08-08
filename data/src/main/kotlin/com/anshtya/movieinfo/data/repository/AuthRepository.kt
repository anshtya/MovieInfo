package com.anshtya.movieinfo.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>

    suspend fun login(
        username: String,
        password: String
    ): Result<Unit>

    suspend fun logout(accountId: Int): Result<Unit>
}