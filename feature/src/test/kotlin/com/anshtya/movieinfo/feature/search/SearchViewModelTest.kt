package com.anshtya.movieinfo.feature.search

import com.anshtya.movieinfo.data.model.SearchItem
import com.anshtya.movieinfo.data.repository.test.TestSearchRepository
import com.anshtya.movieinfo.data.repository.test.TestUserRepository
import com.anshtya.movieinfo.data.repository.test.data.testSearchResults
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
        val errorResult = searchRepository.getSearchSuggestions("test", false)
        viewModel.changeSearchQuery("test")
        advanceUntilIdle()

        assertEquals(
            emptyList<SearchItem>(),
            viewModel.searchSuggestions.value
        )

        assertEquals(
            errorResult.exceptionOrNull()?.message,
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