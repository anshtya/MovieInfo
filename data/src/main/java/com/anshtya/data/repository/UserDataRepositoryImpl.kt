package com.anshtya.data.repository

import com.anshtya.local.datastore.UserPreferencesDataStore
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserDataRepository {
    override val trendingContentFilterIndex = userPreferencesDataStore.trendingContentFilterIndex

    override val freeContentFilterIndex = userPreferencesDataStore.freeContentFilterIndex

    override val popularContentFilterIndex = userPreferencesDataStore.popularContentFilterIndex

    override suspend fun setTrendingContentFilterIndex(index: Int) {
        userPreferencesDataStore.setTrendingContentFilterIndex(index)
    }

    override suspend fun setFreeContentFilterIndex(index: Int) {
        userPreferencesDataStore.setFreeContentFilterIndex(index)
    }

    override suspend fun setPopularContentFilterIndex(index: Int) {
        userPreferencesDataStore.setPopularContentFilterIndex(index)
    }
}