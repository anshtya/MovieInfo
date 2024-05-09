package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.data.repository.AuthRepository

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