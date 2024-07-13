package com.anshtya.movieinfo.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.testing.MainDispatcherRule
import com.anshtya.movieinfo.core.testing.repository.TestLibraryRepository
import com.anshtya.movieinfo.core.testing.util.testLibraryItems
import com.anshtya.movieinfo.feature.you.libraryItemTypeNavigationArgument
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryItemsViewModelTest {
    private val libraryRepository = TestLibraryRepository()
    private lateinit var viewModel: LibraryItemsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
            libraryRepository.movieLibrary,
            viewModel.movieItems.value
        )

        assertEquals(
            libraryRepository.tvLibrary,
            viewModel.tvItems.value
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
            libraryRepository.movieLibrary,
            viewModel.movieItems.value
        )

        assertEquals(
            libraryRepository.tvLibrary,
            viewModel.tvItems.value
        )

        libraryItemTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test delete favorite item`() = runTest{
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

        val testMovie = testLibraryItems[1]
        val testTvShow = testLibraryItems.last()

        viewModel.deleteItem(testMovie)

        assertEquals(
            libraryRepository.movieLibrary.filter { it.id != testMovie.id },
            viewModel.movieItems.value
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            libraryRepository.tvLibrary.filter { it.id != testTvShow.id },
            viewModel.tvItems.value
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

        val testMovie = testLibraryItems[1]
        val testTvShow = testLibraryItems.last()

        viewModel.deleteItem(testMovie)

        assertEquals(
            libraryRepository.movieLibrary.filter { it.id != testMovie.id },
            viewModel.movieItems.value
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            libraryRepository.tvLibrary.filter { it.id != testTvShow.id },
            viewModel.tvItems.value
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
        libraryRepository.generateError(true)

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
            mapOf(libraryItemTypeNavigationArgument to navigationArgument)
        ),
        libraryRepository = libraryRepository
    )
}