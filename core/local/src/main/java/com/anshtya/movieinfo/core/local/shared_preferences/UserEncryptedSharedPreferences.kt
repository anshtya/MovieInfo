package com.anshtya.movieinfo.core.local.shared_preferences

import android.content.SharedPreferences
import javax.inject.Inject

class UserEncryptedSharedPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val USER_SESSION_ID = "user_sesion_id"
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