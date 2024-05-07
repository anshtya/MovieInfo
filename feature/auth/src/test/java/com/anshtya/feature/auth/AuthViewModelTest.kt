package com.anshtya.feature.auth

import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.testing.MainDispatcherRule
import com.anshtya.core.testing.repository.TestAuthRepository
import com.anshtya.core.testing.repository.TestUserRepository
import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.UserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        authRepository = TestAuthRepository()
        userRepository = TestUserRepository()
        viewModel = AuthViewModel(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `test initial state`() = runTest {
        assertEquals(
            viewModel.uiState.value,
            AuthUiState()
        )
    }

    @Test
    fun `test login success`() = runTest {
        val username = "name"
        val password = "1234"

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()


        assertEquals(
            viewModel.uiState.value.isLoggedIn,
            true
        )
    }

    @Test
    fun `test login failure`() = runTest {
        val username = "error"
        val password = "1234"
        val errorResponse = authRepository.login(
            username = username,
            password = password
        ) as NetworkResponse.Error

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()

        assertEquals(
            viewModel.uiState.value.errorMessage,
            errorResponse.errorMessage
        )
        assertNull(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun `test error message reset`() = runTest {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test username change`() = runTest {
        val username = "testuser"

        viewModel.onUsernameChange(username)

        assertEquals(viewModel.uiState.value.username, username)
    }

    @Test
    fun `test password change`() = runTest {
        val password = "testpassword"

        viewModel.onPasswordChange(password)

        assertEquals(viewModel.uiState.value.password, password)
    }
}