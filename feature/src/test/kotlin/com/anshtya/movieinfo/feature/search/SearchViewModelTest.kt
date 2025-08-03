package com.anshtya.movieinfo.feature.search

import com.anshtya.movieinfo.core.data.testdoubles.repository.TestSearchRepository
import com.anshtya.movieinfo.core.data.testdoubles.repository.TestUserRepository
import com.anshtya.movieinfo.core.data.testdoubles.testSearchResults
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SearchItem
import com.anshtya.movieinfo.feature.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val userRepository = TestUserRepository()
    private val searchRepository = TestSearchRepository()
    private lateinit var viewModel: SearchViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
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
        val searchQueryCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchQuery.collect()
        }
        val searchResultCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.searchSuggestions.collect()
        }

        searchRepository.generateError(true)
        val errorResponse = searchRepository.getSearchSuggestions("test", false)
                as NetworkResponse.Error
        viewModel.changeSearchQuery("test")
        advanceUntilIdle()

        assertEquals(
            emptyList<SearchItem>(),
            viewModel.searchSuggestions.value
        )

        assertEquals(
            errorResponse.errorMessage,
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