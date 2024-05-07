package com.anshtya.core.testing.repository

import com.anshtya.core.model.NetworkResponse
import com.anshtya.data.repository.AuthRepository

class TestAuthRepository: AuthRepository {
    override suspend fun login(username: String, password: String): NetworkResponse<Unit> {
        return if (username == "error") {
            NetworkResponse.Error("error")
        } else {
            NetworkResponse.Success(Unit)
        }
    }

    override suspend fun logout(accountId: Int): NetworkResponse<Unit> {
        return if (accountId == 0) {
            NetworkResponse.Error("error")
        } else {
            NetworkResponse.Success(Unit)
        }
    }
}