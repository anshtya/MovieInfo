package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestAuthRepository : AuthRepository {
    private var generateError = false

    private val _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn = _isLoggedIn.asStateFlow()

    override suspend fun login(username: String, password: String): NetworkResponse<Unit> {
        return if (generateError) {
            NetworkResponse.Error()
        } else {
            NetworkResponse.Success(Unit)
        }
    }

    override suspend fun logout(accountId: Int): NetworkResponse<Unit> {
        return if (generateError) {
            NetworkResponse.Error()
        } else {
            NetworkResponse.Success(Unit)
        }
    }

    fun setAuthStatus(isLoggedIn: Boolean) {
        _isLoggedIn.update { isLoggedIn }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}