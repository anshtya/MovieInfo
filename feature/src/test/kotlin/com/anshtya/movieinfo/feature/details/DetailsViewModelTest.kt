package com.anshtya.movieinfo.feature.details

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.repository.DetailsRepository
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.feature.MainDispatcherRule
import com.anshtya.movieinfo.feature.testdata.testLibraryItems
import com.anshtya.movieinfo.feature.testdata.testMovieDetail
import com.anshtya.movieinfo.feature.testdata.testPersonDetails
import com.anshtya.movieinfo.feature.testdata.testTvShowDetails
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailsViewModelTest {
    private val detailsRepository = mockk<DetailsRepository>()
    private val libraryRepository = mockk<LibraryRepository>()
    private val authRepository = mockk<AuthRepository>()
    private val isLoggedInFlow = MutableStateFlow(false)
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        every { authRepository.isLoggedIn } returns isLoggedInFlow.asStateFlow()
        coEvery { libraryRepository.itemInFavoritesExists(any(), any()) } returns false
        coEvery { libraryRepository.itemInWatchlistExists(any(), any()) } returns false
        coEvery { libraryRepository.addOrRemoveFavorite(any()) } just Runs
        coEvery { libraryRepository.addOrRemoveFromWatchlist(any()) } just Runs

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
        coEvery { detailsRepository.getMovieDetails(any()) } returns Result.success(testMovieDetail)
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
        coEvery { detailsRepository.getTvShowDetails(any()) } returns Result.success(testTvShowDetails)
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
        coEvery { detailsRepository.getPersonDetails(any()) } returns Result.success(testPersonDetails)
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
        val exception = Exception("An error occurred")
        coEvery { detailsRepository.getMovieDetails(any()) } returns Result.failure(exception)
        viewModel = createViewModel(navigationArgument = "100,${MediaType.MOVIE}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            exception.message,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in tv show details content state`() = runTest {
        val exception = Exception("An error occurred")
        coEvery { detailsRepository.getTvShowDetails(any()) } returns Result.failure(exception)
        viewModel = createViewModel(navigationArgument = "101,${MediaType.TV}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            exception.message,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in person details content state`() = runTest {
        val exception = Exception("An error occurred")
        coEvery { detailsRepository.getPersonDetails(any()) } returns Result.failure(exception)
        viewModel = createViewModel(navigationArgument = "102,${MediaType.PERSON}")

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            exception.message,
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test favorite`() = runTest {
        val libraryItem = testLibraryItems[0].copy(id = 0)

        isLoggedInFlow.value = true
        viewModel.addOrRemoveFavorite(libraryItem)

        assertTrue(viewModel.uiState.value.markedFavorite)

        isLoggedInFlow.value = false
        viewModel.addOrRemoveFavorite(libraryItem)

        assertTrue(viewModel.uiState.value.showSignInSheet)

        isLoggedInFlow.value = true
        coEvery { libraryRepository.addOrRemoveFavorite(any()) } throws IOException()
        viewModel.addOrRemoveFavorite(libraryItem)

        assertEquals(
            "An error occurred",
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `test watchlist`() = runTest {
        val libraryItem = testLibraryItems[0].copy(id = 0)

        isLoggedInFlow.value = true
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertTrue(viewModel.uiState.value.savedInWatchlist)

        isLoggedInFlow.value = false
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertTrue(viewModel.uiState.value.showSignInSheet)

        isLoggedInFlow.value = true
        coEvery { libraryRepository.addOrRemoveFromWatchlist(any()) } throws IOException()
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
            mapOf("id" to navigationArgument)
        ),
        detailsRepository = detailsRepository,
        libraryRepository = libraryRepository,
        authRepository = authRepository
    )
}
