package com.anshtya.data.model

import com.anshtya.network.model.NetworkSearchSuggestion
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchSuggestionTest {
    @Test
    fun `NetworkSearchSuggestion can be mapped to SearchSuggestion`() {
        val networkSearchSuggestion = NetworkSearchSuggestion(
            id = 1,
            name = null,
            posterPath = "path",
            profilePath = null,
            title = "title"
        )
        val searchSuggestion = networkSearchSuggestion.asModel()
        assertEquals(searchSuggestion.id, networkSearchSuggestion.id)
        assertEquals(searchSuggestion.name, networkSearchSuggestion.title)
        assertEquals(searchSuggestion.imagePath, networkSearchSuggestion.posterPath)
    }

    @Test
    fun `when title is null then value of name is stored`() {
        val networkSearchSuggestion = NetworkSearchSuggestion(
            id = 1,
            name = "name",
            posterPath = "path",
            profilePath = "path",
            title = null
        )
        val searchSuggestion = networkSearchSuggestion.asModel()
        assertEquals(searchSuggestion.name, networkSearchSuggestion.name)
    }

    @Test
    fun `when title and name are null then empty string is stored`() {
        val networkSearchSuggestion = NetworkSearchSuggestion(
            id = 1,
            name = null,
            posterPath = "path",
            profilePath = "path",
            title = null
        )
        val searchSuggestion = networkSearchSuggestion.asModel()
        assertEquals(searchSuggestion.name, "")
    }

    @Test
    fun `when posterPath is null then value of profilePath is stored`() {
        val networkSearchSuggestion = NetworkSearchSuggestion(
            id = 1,
            name = "name",
            posterPath = null,
            profilePath = "path",
            title = "title"
        )
        val searchSuggestion = networkSearchSuggestion.asModel()
        assertEquals(searchSuggestion.imagePath, networkSearchSuggestion.profilePath)
    }

    @Test
    fun `when profilePath and posterPath is null then empty string is stored`() {
        val networkSearchSuggestion = NetworkSearchSuggestion(
            id = 1,
            name = "name",
            posterPath = null,
            profilePath = null,
            title = "title"
        )
        val searchSuggestion = networkSearchSuggestion.asModel()
        assertEquals(searchSuggestion.imagePath, "")
    }
}