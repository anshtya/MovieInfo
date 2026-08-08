package com.anshtya.movieinfo.feature.you

import com.anshtya.movieinfo.data.model.SelectedDarkMode
import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.repository.UserRepository
import com.anshtya.movieinfo.feature.MainDispatcherRule
import com.anshtya.movieinfo.feature.testdata.testAccountDetails
import com.anshtya.movieinfo.feature.testdata.testUserData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class YouViewModelTest {
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()
    private val isLoggedInFlow = MutableStateFlow(false)
    private val userDataFlow = MutableStateFlow(testUserData)
    private lateinit var viewModel: YouViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        every { authRepository.isLoggedIn } returns isLoggedInFlow.asStateFlow()
        every { userRepository.userData } returns userDataFlow
        coEvery { userRepository.getAccountDetails() } returns testAccountDetails
        coEvery {
            userRepository.setDynamicColorPreference(any())
        } answers {
            userDataFlow.update { it.copy(useDynamicColor = firstArg()) }
        }
        coEvery {
            userRepository.setAdultResultPreference(any())
        } answers {
            userDataFlow.update { it.copy(includeAdultResults = firstArg()) }
        }
        coEvery {
            userRepository.setDarkModePreference(any())
        } answers {
            userDataFlow.update { it.copy(darkMode = firstArg()) }
        }

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

        isLoggedInFlow.value = true
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
        val exception = Exception("An error occurred")

        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        isLoggedInFlow.value = true
        coEvery { authRepository.logout(any()) } returns Result.failure(exception)

        viewModel.logOut()

        assertEquals(
            exception.message,
            viewModel.uiState.value.errorMessage
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }

    @Test
    fun `test refresh error`() = runTest {
        val exception = Exception("An error occurred")

        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        isLoggedInFlow.value = true
        coEvery { userRepository.updateAccountDetails(any()) } returns Result.failure(exception)

        viewModel.onRefresh()

        assertEquals(
            exception.message,
            viewModel.uiState.value.errorMessage
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }
}
