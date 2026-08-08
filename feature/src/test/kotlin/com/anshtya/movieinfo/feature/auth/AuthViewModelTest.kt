package com.anshtya.movieinfo.feature.auth

import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.repository.UserRepository
import com.anshtya.movieinfo.feature.MainDispatcherRule
import com.anshtya.movieinfo.feature.testdata.testUserData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val userDataFlow = MutableStateFlow(testUserData)
    private lateinit var viewModel: AuthViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { userRepository.userData } returns userDataFlow
        coEvery { userRepository.setHideOnboarding(true) } answers {
            userDataFlow.update { it.copy(hideOnboarding = true) }
        }

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
    fun `test login success when user onboards`() {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)

        val username = "name"
        val password = "1234"

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()

        assertFalse(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun `test login success when after onboarding`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)

        userRepository.setHideOnboarding(true)
        viewModel = AuthViewModel(
            authRepository = authRepository,
            userRepository = userRepository
        )

        val username = "name"
        val password = "1234"

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()

        assertTrue(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun `test login failure`() = runTest {
        val username = "error"
        val password = "1234"
        val exception = Exception("An error occurred")

        coEvery {
            authRepository.login(username = username, password = password)
        } returns Result.failure(exception)

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.logIn()

        assertEquals(
            exception.message,
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
