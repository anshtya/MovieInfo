package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.core.model.NetworkResponse

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String
    ): NetworkResponse<Unit>

    suspend fun logout(accountId: Int): NetworkResponse<Unit>
}