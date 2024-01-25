package com.anshtya.data.repository

import com.anshtya.data.model.NetworkResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): NetworkResponse<Unit>
    suspend fun logout(): NetworkResponse<Unit>
    suspend fun updateAccountDetails(): NetworkResponse<Unit>
}