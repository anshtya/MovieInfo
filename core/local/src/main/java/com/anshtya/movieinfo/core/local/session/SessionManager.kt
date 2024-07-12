package com.anshtya.movieinfo.core.local.session

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val USER_SESSION_ID = "user_sesion_id"
    }

    val isLoggedIn: Flow<Boolean> = callbackFlow {
        trySend(getSessionId() != null)

        val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == USER_SESSION_ID) {
                trySend(getSessionId() != null)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener)
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefListener)
        }
    }

    fun storeSessionId(sessionId: String) {
        sharedPreferences.edit()
            .putString(USER_SESSION_ID, sessionId)
            .apply()
    }

    fun getSessionId(): String? {
        return sharedPreferences.getString(USER_SESSION_ID, null)
    }

    fun deleteSessionId() {
        sharedPreferences.edit()
            .putString(USER_SESSION_ID, null)
            .apply()
    }
}