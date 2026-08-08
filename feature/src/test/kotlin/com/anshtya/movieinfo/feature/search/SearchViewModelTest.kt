package com.anshtya.movieinfo.feature.search

import com.anshtya.movieinfo.data.model.SearchItem
import com.anshtya.movieinfo.data.repository.SearchRepository
import com.anshtya.movieinfo.data.repository.UserRepository
import com.anshtya.movieinfo.feature.MainDispatcherRule
import com.anshtya.movieinfo.feature.testdata.testSearchResults
import com.anshtya.movieinfo.feature.testdata.testUserData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val userRepository = mockk<UserRepository>()
    private val searchRepository = mockk<SearchRepository>()
    private val userDataFlow = MutableStateFlow(testUserData)
    private lateinit var viewModel: SearchViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        every { userRepository.userData } returns userDataFlow

        viewModel = SearchViewModel(
            userRepository = userRepository,
            searchRepository = searchRepository
        )
    }

    @Test
    fun `test initial state`() {
        assertEquals(
            "",
            viewModel.searchQuery.value
        )

        assertNull(viewModel.errorMessage.value)

        assertEquals(
            emptyList<SearchItem>(),
            viewModel.searchSuggestions.value
        )
    }

    @Test
    fun `test search result when query entered`() = runTest {
        coEvery {
            searchRepository.getSearchSuggestions(any(), any())
        } returns Result.success(testSearchResults)

        val searchQueryCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchQuery.collect()
        }
        val searchResultCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchSuggestions.collect()
        }

        viewModel.changeSearchQuery("test")
        advanceUntilIdle()

        assertEquals(
            testSearchResults,
            viewModel.searchSuggestions.value
        )

        searchQueryCollectJob.cancel()
        searchResultCollectJob.cancel()
    }

    @Test
    fun `test search result error`() = runTest {
        val exception = Exception("An error occurred")
        coEvery {
            searchRepository.getSearchSuggestions(any(), any())
        } returns Result.failure(exception)

        val searchQueryCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchQuery.collect()
        }
        val searchResultCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchSuggestions.collect()
        }

        viewModel.changeSearchQuery("test")
        advanceUntilIdle()

        assertEquals(
            emptyList<SearchItem>(),
            viewModel.searchSuggestions.value
        )

        assertEquals(
            exception.message,
            viewModel.errorMessage.value
        )

        searchQueryCollectJob.cancel()
        searchResultCollectJob.cancel()
    }

    @Test
    fun `test error message reset`() {
        viewModel.onErrorShown()

        assertNull(viewModel.errorMessage.value)
    }
}
