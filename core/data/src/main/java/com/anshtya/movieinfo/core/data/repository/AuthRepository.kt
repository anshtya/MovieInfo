package com.anshtya.movieinfo.core.data.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>

    suspend fun login(
        username: String,
        password: String
    ): NetworkResponse<Unit>

    suspend fun logout(accountId: Int): NetworkResponse<Unit>
}