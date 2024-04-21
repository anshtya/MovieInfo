package com.anshtya.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.anshtya.core.network.model.content.NetworkContentItem
import com.anshtya.core.testing.rules.MainDispatcherRule
import com.anshtya.data.test_doubles.FakeTmdbApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PagingSourceTest {
    private lateinit var fakeTmdbApi: FakeTmdbApi
    private lateinit var pagingSource: MovieListPagingSource

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        fakeTmdbApi = FakeTmdbApi(mockMovieListResponse)
        pagingSource = MovieListPagingSource(
            tmdbApi = fakeTmdbApi,
            categoryName = ""
        )
    }

    @Test
    fun `load items from PagingSource`() = runTest {
        val pager = TestPager(
            config = PagingConfig(pageSize = 1),
            pagingSource = pagingSource
        )
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertEquals(
            mockMovieListResponse.results.map(NetworkContentItem::asModel),
            result.data
        )
    }
}