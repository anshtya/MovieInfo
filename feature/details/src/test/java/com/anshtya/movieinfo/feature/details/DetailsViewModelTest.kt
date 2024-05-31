package com.anshtya.movieinfo.feature.details

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.testing.MainDispatcherRule
import com.anshtya.movieinfo.core.testing.repository.TestDetailsRepository
import com.anshtya.movieinfo.core.testing.repository.TestLibraryRepository
import com.anshtya.movieinfo.core.testing.repository.TestUserRepository
import com.anshtya.movieinfo.core.testing.util.testLibraryItems
import com.anshtya.movieinfo.core.testing.util.testMovieDetail
import com.anshtya.movieinfo.core.testing.util.testPersonDetails
import com.anshtya.movieinfo.core.testing.util.testTvShowDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
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
    private val userRepository = TestUserRepository()
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
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "100,${MediaType.MOVIE}")
            )
        )

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
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "101,${MediaType.TV}")
            )
        )

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
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "102,${MediaType.PERSON}")
            )
        )

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
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "100,${MediaType.MOVIE}")
            )
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            "error",
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in tv show details content state`() = runTest {
        detailsRepository.generateError(true)
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "101,${MediaType.TV}")
            )
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            "error",
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test error in person details content state`() = runTest {
        detailsRepository.generateError(true)
        viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(idNavigationArgument to "102,${MediaType.PERSON}")
            )
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.contentDetailsUiState.collect()
        }

        assertEquals(
            ContentDetailUiState.Empty,
            viewModel.contentDetailsUiState.value
        )

        assertEquals(
            "error",
            viewModel.uiState.value.errorMessage
        )

        collectJob.cancel()
    }

    @Test
    fun `test favorite`() = runTest {
        val libraryItem = testLibraryItems[0]
        viewModel.addOrRemoveFavorite(libraryItem)

        assertEquals(
            true,
            viewModel.uiState.value.markedFavorite
        )

        userRepository.setUserSessionId(null)
        viewModel.addOrRemoveFavorite(libraryItem)

        assertEquals(
            true,
            viewModel.uiState.value.showSignInSheet
        )

        userRepository.setUserSessionId("id")
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
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertEquals(
            true,
            viewModel.uiState.value.savedInWatchlist
        )

        userRepository.setUserSessionId(null)
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertEquals(
            true,
            viewModel.uiState.value.showSignInSheet
        )

        userRepository.setUserSessionId("id")
        libraryRepository.generateError(true)
        viewModel.addOrRemoveFromWatchlist(libraryItem)

        assertEquals(
            "An error occurred",
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `test error message reset`() = runTest {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test bottom sheet reset`() = runTest {
        viewModel.onHideBottomSheet()

        assertFalse(viewModel.uiState.value.showSignInSheet)
    }

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf(idNavigationArgument to ""))
    ) = DetailsViewModel(
        savedStateHandle = savedStateHandle,
        detailsRepository = detailsRepository,
        libraryRepository = libraryRepository,
        userRepository = userRepository
    )
}