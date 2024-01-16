package com.anshtya.data.model

import com.anshtya.core.network.model.search.NetworkSearchItem
import com.anshtya.core.network.model.search.asModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchItemTest {
    @Test
    fun `NetworkSearchItem can be mapped to SearchItem`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = null,
            posterPath = "path",
            profilePath = null,
            title = "title",
            mediaType = ""
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.name, networkSearchItem.title)
        assertEquals(searchItem.imagePath, networkSearchItem.posterPath)
    }

    @Test
    fun `when title is null then value of name is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = "name",
            posterPath = "path",
            profilePath = null,
            title = null,
            mediaType = ""
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.name, networkSearchItem.name)
    }

    @Test
    fun `when title and name are null then empty string is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = null,
            posterPath = "path",
            profilePath = null,
            title = null,
            mediaType = ""
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.name, "")
    }

    @Test
    fun `when posterPath is null then empty string is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = "name",
            posterPath = null,
            profilePath = null,
            title = "title",
            mediaType = ""
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.imagePath, "")
    }
}