package com.anshtya.movieinfo.feature.details

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.testing.MainDispatcherRule
import com.anshtya.movieinfo.core.testing.repository.TestAuthRepository
import com.anshtya.movieinfo.core.testing.repository.TestDetailsRepository
import com.anshtya.movieinfo.core.testing.repository.TestLibraryRepository
import com.anshtya.movieinfo.core.testing.util.testLibraryItems
import com.anshtya.movieinfo.core.testing.util.testMovieDetail
import com.anshtya.movieinfo.core.testing.util.testPersonDetails
import com.anshtya.movieinfo.core.testing.util.testTvShowDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
    private val detailsRepository = TestDetailsRepository()
    private val libraryRepository = TestLibraryRepository()
    private val authRepository = TestAuthRepository()
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = createViewModel()
    }

    @Test
    fun `test initial state`() = runTest {
        assertEquals(
            DetailsUiState(),
            viewModel.uiState.value
        )
    }

    @Test
    fun `test empty content state`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test movie details content state`() = runTest {
        viewModel = createViewModel(navigationArgument = "100,${MediaType.MOVIE}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Movie(data = testMovieDetail),
            viewModel.contentDetailsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test tv show details content state`() = runTest {
        viewModel = createViewModel(navigationArgument = "101,${MediaType.TV}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.TV(data = testTvShowDetails),
            viewModel.contentDetailsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test person details content state`() = runTest {
        viewModel = createViewModel(navigationArgument = "102,${MediaType.PERSON}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Person(data = testPersonDetails),
            viewModel.contentDetailsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in movie details content state`() = runTest {
        detailsRepository.generateError(true)
        val errorResponse = detailsRepository.getMovieDetails(0) as NetworkResponse.Error
        viewModel = createViewModel(navigationArgument = "100,${MediaType.MOVIE}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in tv show details content state`() = runTest {
        detailsRepository.generateError(true)
        val errorResponse = detailsRepository.getTvShowDetails(0) as NetworkResponse.Error
        viewModel = createViewModel(navigationArgument = "101,${MediaType.TV}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in person details content state`() = runTest {
        detailsRepository.generateError(true)
        val errorResponse = detailsRepository.getPersonDetails(0) as NetworkResponse.Error
        viewModel = createViewModel(navigationArgument = "102,${MediaType.PERSON}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            errorResponse.errorMessage,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test favorite`() = runTest {
        val libraryItem = testLibraryItems[0]
        authRepository.setAuthStatus(true)
        viewModel.addOrRemoveFavorite(libraryItem)

        assertTrue(viewModel.uiState.value.markedFavorite)

        authRepository.setAuthStatus(false)
        viewModel.addOrRemoveFavorite(libraryItem)

        assertTrue(viewModel.uiState.value.showSignInSheet)

        authRepository.setAuthStatus(true)
        libraryRepository.generateError(true)
        viewModel.addOrRemoveFavorite(libraryItem)

        assertEquals(
            "An error occurred",
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `test watchlist`() = runTest {
        val libraryItem = testLibraryItems[0]
        authRepository.setAuthStatus(true)
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertTrue(viewModel.uiState.value.savedInWatchlist)

        authRepository.setAuthStatus(false)
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertTrue(viewModel.uiState.value.showSignInSheet)

        authRepository.setAuthStatus(true)
        libraryRepository.generateError(true)
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertEquals(
            "An error occurred",
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `test error message reset`() {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test bottom sheet reset`() {
        viewModel.onHideBottomSheet()

        assertFalse(viewModel.uiState.value.showSignInSheet)
    }

    private fun createViewModel(
        navigationArgument: String = ""
    ) = DetailsViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(idNavigationArgument to navigationArgument)
        ),
        detailsRepository = detailsRepository,
        libraryRepository = libraryRepository,
        authRepository = authRepository
    )
}