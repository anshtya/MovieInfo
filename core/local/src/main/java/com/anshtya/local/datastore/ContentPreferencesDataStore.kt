package com.anshtya.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContentPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val TRENDING_FILTER_INDEX = intPreferencesKey("trending_filter_index")
        val TRENDING_FILTER_STRING = stringPreferencesKey("trending_filter_string")
        val FREE_FILTER_INDEX = intPreferencesKey("free_filter_index")
        val FREE_FILTER_STRING = stringPreferencesKey("free_filter_string")
        val POPULAR_FILTER_INDEX = intPreferencesKey("popular_filter_index")
        val POPULAR_FILTER_STRING = stringPreferencesKey("popular_filter_string")
        val DB_LAST_UPDATE_TIME = longPreferencesKey("db_last_update_time")
    }

    val trendingContentFilterIndex = dataStore.data.map { preferences ->
        preferences[TRENDING_FILTER_INDEX] ?: 0
    }

    val trendingContentFilterString = dataStore.data.map { preferences ->
        preferences[TRENDING_FILTER_STRING] ?: ""
    }

    val freeContentFilterIndex = dataStore.data.map { preferences ->
        preferences[FREE_FILTER_INDEX] ?: 0
    }

    val freeContentFilterString = dataStore.data.map { preferences ->
        preferences[FREE_FILTER_STRING] ?: ""
    }

    val popularContentFilterIndex = dataStore.data.map { preferences ->
        preferences[POPULAR_FILTER_INDEX] ?: 0
    }

    val popularContentFilterString = dataStore.data.map { preferences ->
        preferences[POPULAR_FILTER_STRING] ?: ""
    }

    val dbLastUpdateTime = dataStore.data.map { preferences ->
        preferences[DB_LAST_UPDATE_TIME] ?: 0
    }

    suspend fun setTrendingContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[TRENDING_FILTER_INDEX] = index
        }
    }

    suspend fun setTrendingContentFilterString(string: String) {
        dataStore.edit { preferences ->
            preferences[TRENDING_FILTER_STRING] = string
        }
    }

    suspend fun setFreeContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[FREE_FILTER_INDEX] = index
        }
    }

    suspend fun setFreeContentFilterString(string: String) {
        dataStore.edit { preferences ->
            preferences[FREE_FILTER_STRING] = string
        }
    }

    suspend fun setPopularContentFilterIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[POPULAR_FILTER_INDEX] = index
        }
    }

    suspend fun setPopularContentFilterString(string: String) {
        dataStore.edit { preferences ->
            preferences[POPULAR_FILTER_STRING] = string
        }
    }

    suspend fun setDbLastUpdateTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[DB_LAST_UPDATE_TIME] = time
        }
    }
}