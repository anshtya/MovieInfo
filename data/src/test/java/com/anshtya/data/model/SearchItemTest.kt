package com.anshtya.data.model

import com.anshtya.network.model.NetworkSearchItem
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchItemTest {
    @Test
    fun `NetworkSearchItem can be mapped to SearchItem`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = null,
            overview = "",
            posterPath = "path",
            title = "title"
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.id, networkSearchItem.id)
        assertEquals(searchItem.name, networkSearchItem.title)
        assertEquals(searchItem.posterPath, networkSearchItem.posterPath)
    }

    @Test
    fun `when title is null then value of name is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = "name",
            overview = "overview",
            posterPath = "path",
            title = null
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.name, networkSearchItem.name)
    }

    @Test
    fun `when title and name are null then empty string is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = null,
            overview = "overview",
            posterPath = "path",
            title = null
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.name, "")
    }

    @Test
    fun `when posterPath is null then empty string is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = "name",
            overview = "overview",
            posterPath = null,
            title = "title"
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.posterPath, "")
    }

    @Test
    fun `when overview is null then empty string is stored`() {
        val networkSearchItem = NetworkSearchItem(
            id = 1,
            name = null,
            overview = null,
            posterPath = "path",
            title = "title"
        )
        val searchItem = networkSearchItem.asModel()
        assertEquals(searchItem.overview, "")
    }
}