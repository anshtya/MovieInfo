package com.anshtya.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val TRENDING_FILTER_INDEX = intPreferencesKey("trending_filter_index")
        val FREE_FILTER_INDEX = intPreferencesKey("free_filter_index")
        val POPULAR_FILTER_INDEX = intPreferencesKey("popular_filter_index")
    }

    val trendingContentFilterIndex = dataStore.data.map { preferences ->
        preferences[TRENDING_FILTER_INDEX] ?: 0
    }

    val freeContentFilterIndex = dataStore.data.map { preferences ->
        preferences[FREE_FILTER_INDEX] ?: 0
    }

    val popularContentFilterIndex = dataStore.data.map { preferences ->
        preferences[POPULAR_FILTER_INDEX] ?: 0
    }

    suspend fun setTrendingContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[TRENDING_FILTER_INDEX] = index
        }
    }

    suspend fun setFreeContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[FREE_FILTER_INDEX] = index
        }
    }

    suspend fun setPopularContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[POPULAR_FILTER_INDEX] = index
        }
    }
}