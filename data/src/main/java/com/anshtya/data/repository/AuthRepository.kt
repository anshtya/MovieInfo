package com.anshtya.data.repository

import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.util.Synchronizer

interface AuthRepository: Synchronizer {
    suspend fun login(username: String, password: String): NetworkResponse<Unit>
    suspend fun logout(): NetworkResponse<Unit>
}