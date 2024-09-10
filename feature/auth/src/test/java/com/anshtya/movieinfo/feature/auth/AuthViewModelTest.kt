package com.anshtya.movieinfo.feature.auth

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.testing.MainDispatcherRule
import com.anshtya.movieinfo.data.testdoubles.repository.TestAuthRepository
import com.anshtya.movieinfo.data.testdoubles.repository.TestUserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    private val authRepository = TestAuthRepository()
    private val userRepository = TestUserRepository()
    private lateinit var viewModel: AuthViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = AuthViewModel(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `test initial state`() {
        assertEquals(
            AuthUiState(),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `test login failure`() = runTest {
        val username = "error"
        val password = "1234"

        authRepository.generateError(true)
        val errorResponse = authRepository.login(
            username = username,
            password = password
        ) as NetworkResponse.Error

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `test error message reset`() {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test username change`() {
        val username = "testuser"

        viewModel.onUsernameChange(username)

        assertEquals(
            username,
            viewModel.uiState.value.username
        )
    }

    @Test
    fun `test password change`() {
        val password = "testpassword"

        viewModel.onPasswordChange(password)

        assertEquals(
            password,
            viewModel.uiState.value.password
        )
    }
}