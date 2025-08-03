package com.anshtya.movieinfo.data.local.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val datastore: DataStore<Preferences>
) {
    companion object {
        val USER_SESSION_ID = stringPreferencesKey("user_session_id")
    }

    val isLoggedIn: Flow<Boolean> = datastore.data.map {
        it[USER_SESSION_ID]?.isNotEmpty() ?: false
    }

    suspend fun storeSessionId(sessionId: String) {
        datastore.edit {
            it[USER_SESSION_ID] = sessionId
        }
    }

    suspend fun getSessionId(): String? =
        datastore.data.map { it[USER_SESSION_ID] }.first()

    suspend fun deleteSessionId() {
        datastore.edit {
            it[USER_SESSION_ID] = ""
        }
    }
}