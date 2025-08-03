package com.anshtya.movieinfo.core.data.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import com.anshtya.movieinfo.core.model.user.AccountDetails
import com.anshtya.movieinfo.core.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userData: Flow<UserData>

    suspend fun getAccountDetails(): AccountDetails?

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setAdultResultPreference(includeAdultResults: Boolean)

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode)

    suspend fun updateAccountDetails(accountId: Int): NetworkResponse<Unit>

    suspend fun setHideOnboarding(hideOnboarding: Boolean)
}