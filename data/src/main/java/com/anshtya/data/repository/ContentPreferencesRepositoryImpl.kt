package com.anshtya.data.repository

import com.anshtya.core.local.datastore.ContentPreferencesDataStore
import javax.inject.Inject

internal class ContentPreferencesRepositoryImpl @Inject constructor(
    private val contentPreferencesDataStore: ContentPreferencesDataStore
) : ContentPreferencesRepository {
    override val trendingContentFilterIndex = contentPreferencesDataStore.trendingContentFilterIndex

    override val freeContentFilterIndex = contentPreferencesDataStore.freeContentFilterIndex

    override val popularContentFilterIndex = contentPreferencesDataStore.popularContentFilterIndex

    override suspend fun setTrendingContentFilterIndex(index: Int) {
        contentPreferencesDataStore.setTrendingContentFilterIndex(index)
    }

    override suspend fun setFreeContentFilterIndex(index: Int) {
        contentPreferencesDataStore.setFreeContentFilterIndex(index)
    }

    override suspend fun setPopularContentFilterIndex(index: Int) {
        contentPreferencesDataStore.setPopularContentFilterIndex(index)
    }
}