package com.anshtya.data.repository

import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userData: Flow<UserData>

    val accountDetails: Flow<AccountDetails?>

    fun isSignedIn(): Boolean

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setAdultResultPreference(includeAdultResults: Boolean)

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode)

    suspend fun updateAccountDetails(accountId: Int): NetworkResponse<Unit>
}