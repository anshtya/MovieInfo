package com.anshtya.movieinfo.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.data.model.library.LibraryItemType
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.feature.MainDispatcherRule
import com.anshtya.movieinfo.feature.testdata.testLibraryItems
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
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
class LibraryItemsViewModelTest {
    private val libraryRepository = mockk<LibraryRepository>()
    private val moviesFlow = MutableStateFlow(listOf(testLibraryItems[0]))
    private val tvShowsFlow = MutableStateFlow(listOf(testLibraryItems[1]))
    private lateinit var viewModel: LibraryItemsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        every { libraryRepository.favoriteMovies } returns moviesFlow.asStateFlow()
        every { libraryRepository.moviesWatchlist } returns moviesFlow.asStateFlow()
        every { libraryRepository.favoriteTvShows } returns tvShowsFlow.asStateFlow()
        every { libraryRepository.tvShowsWatchlist } returns tvShowsFlow.asStateFlow()
        coEvery { libraryRepository.addOrRemoveFavorite(any()) } answers { removeItem(firstArg()) }
        coEvery { libraryRepository.addOrRemoveFromWatchlist(any()) } answers { removeItem(firstArg()) }
    }

    private fun removeItem(libraryItem: LibraryItem) {
        when (enumValueOf<MediaType>(libraryItem.mediaType.uppercase())) {
            MediaType.MOVIE -> moviesFlow.update { it - libraryItem }
            MediaType.TV -> tvShowsFlow.update { it - libraryItem }
            else -> {}
        }
    }

    @Test
    fun `test favorite items initialization`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryItemType.FAVORITE.name)

        val libraryItemTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryItemType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        assertEquals(
            LibraryItemType.FAVORITE,
            viewModel.libraryItemType.value
        )

        assertEquals(
            1,
            viewModel.movieItems.value.size
        )

        assertEquals(
            1,
            viewModel.tvItems.value.size
        )

        libraryItemTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test watchlist items initialization`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryItemType.WATCHLIST.name)

        val libraryItemTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryItemType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        assertEquals(
            LibraryItemType.WATCHLIST,
            viewModel.libraryItemType.value
        )

        assertEquals(
            1,
            viewModel.movieItems.value.size
        )

        assertEquals(
            1,
            viewModel.tvItems.value.size
        )

        libraryItemTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test delete favorite item`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryItemType.FAVORITE.name)

        val libraryItemTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryItemType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        val testMovie = testLibraryItems[0]
        val testTvShow = testLibraryItems[1]

        viewModel.deleteItem(testMovie)

        assertEquals(
            0,
            viewModel.movieItems.value.size
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            0,
            viewModel.tvItems.value.size
        )

        libraryItemTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test delete watchlist item`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryItemType.WATCHLIST.name)

        val libraryItemTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryItemType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        val testMovie = testLibraryItems[0]
        val testTvShow = testLibraryItems[1]

        viewModel.deleteItem(testMovie)
        assertEquals(
            0,
            viewModel.movieItems.value.size
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            0,
            viewModel.tvItems.value.size
        )

        libraryItemTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test error in when deleting item`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryItemType.FAVORITE.name)

        val libraryItemTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryItemType.collect()
        }
        val errorCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.errorMessage.collect()
        }

        val testItem = testLibraryItems[0]
        coEvery { libraryRepository.addOrRemoveFavorite(any()) } throws IOException()

        viewModel.deleteItem(testItem)

        assertEquals(
            "An error occurred",
            viewModel.errorMessage.value
        )

        libraryItemTypeCollectJob.cancel()
        errorCollectJob.cancel()
    }

    @Test
    fun `test error message reset`() {
        viewModel = createViewModel(navigationArgument = LibraryItemType.FAVORITE.name)

        viewModel.onErrorShown()

        assertNull(viewModel.errorMessage.value)
    }

    private fun createViewModel(
        navigationArgument: String
    ) = LibraryItemsViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf("type" to navigationArgument)
        ),
        libraryRepository = libraryRepository
    )
}
