package com.anshtya.data.repository

import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.model.content.FreeContentType
import com.anshtya.core.model.content.PopularContentType
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.content.TrendingContentTimeWindow
import com.anshtya.core.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setAdultResultPreference(includeAdultResults: Boolean)
    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode)
    suspend fun setFreeContentPreference(selectedFreeContentType: FreeContentType)
    suspend fun setPopularContentPreference(selectedPopularContentType: PopularContentType)
    suspend fun setTrendingContentPreference(
        selectedTrendingContentTimeWindow: TrendingContentTimeWindow
    )
    suspend fun saveAccountDetails(accountDetails: AccountDetails)
    suspend fun removeAccountDetails()
}