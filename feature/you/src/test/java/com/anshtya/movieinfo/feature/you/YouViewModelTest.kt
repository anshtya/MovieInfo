package com.anshtya.movieinfo.feature.you

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import com.anshtya.movieinfo.core.testing.MainDispatcherRule
import com.anshtya.movieinfo.data.testdoubles.repository.TestAuthRepository
import com.anshtya.movieinfo.data.testdoubles.repository.TestUserRepository
import com.anshtya.movieinfo.data.testdoubles.repository.testAccountDetails
import com.anshtya.movieinfo.data.testdoubles.repository.testUserData
import junit.framework.TestCase
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class YouViewModelTest {
    private val authRepository = TestAuthRepository()
    private val userRepository = TestUserRepository()
    private lateinit var viewModel: YouViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = YouViewModel(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `test initial state`() {
        TestCase.assertEquals(
            YouUiState(),
            viewModel.uiState.value
        )
    }

    @Test
    fun `test account details`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        authRepository.setAuthStatus(isLoggedIn = true)
        assertEquals(
            YouUiState(accountDetails = testAccountDetails),
            viewModel.uiState.value
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }

    @Test
    fun `test user settings`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.userSettings.collect()
        }

        assertEquals(
            UserSettings(
                useDynamicColor = testUserData.useDynamicColor,
                includeAdultResults = testUserData.includeAdultResults,
                darkMode = testUserData.darkMode
            ),
            viewModel.userSettings.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test error message reset`() {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test preference updates`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.userSettings.collect()
        }

        viewModel.setDynamicColorPreference(true)
        assertEquals(
            true,
            viewModel.userSettings.value?.useDynamicColor
        )

        viewModel.setDarkModePreference(SelectedDarkMode.LIGHT)
        assertEquals(
            SelectedDarkMode.LIGHT,
            viewModel.userSettings.value?.darkMode
        )

        viewModel.setAdultResultPreference(true)
        assertEquals(
            true,
            viewModel.userSettings.value?.includeAdultResults
        )

        collectJob.cancel()
    }

    @Test
    fun `test logout error`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        with(authRepository) {
            setAuthStatus(true)
            generateError(true)
        }

        val errorResponse = authRepository.logout(0) as NetworkResponse.Error
        viewModel.logOut()

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }

    @Test
    fun `test refresh error`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        authRepository.setAuthStatus(true)
        userRepository.generateError(true)
        val errorResponse = userRepository.updateAccountDetails(0)
                as NetworkResponse.Error
        viewModel.onRefresh()

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }
}