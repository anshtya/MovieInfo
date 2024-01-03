package com.anshtya.core.local.datastore

import androidx.datastore.core.DataStore
import com.anshtya.core.local.model.asModel
import com.anshtya.core.local.proto.AccountDetailsProto
import com.anshtya.core.local.proto.DarkMode
import com.anshtya.core.local.proto.FreeContent
import com.anshtya.core.local.proto.PopularContent
import com.anshtya.core.local.proto.TrendingContent
import com.anshtya.core.local.proto.UserPreferences
import com.anshtya.core.local.proto.copy
import com.anshtya.core.model.AccountDetails
import com.anshtya.core.model.FreeContentType
import com.anshtya.core.model.PopularContentType
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.TrendingContentTimeWindow
import com.anshtya.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    private val accountDetailsDefaultInstance = AccountDetailsProto.getDefaultInstance()
    val userData = userPreferences.data
        .map {
            UserData(
                useDynamicColor = it.useDynamicColor,
                includeAdultResults = it.includeAdultResults,
                isLoggedIn = it.accountDetails != accountDetailsDefaultInstance,
                darkMode = when (it.darkMode) {
                    null,
                    DarkMode.UNRECOGNIZED,
                    DarkMode.DARK_MODE_SYSTEM
                    -> SelectedDarkMode.SYSTEM

                    DarkMode.DARK_MODE_DARK -> SelectedDarkMode.DARK
                    DarkMode.DARK_MODE_LIGHT -> SelectedDarkMode.LIGHT
                },
                selectedFreeContentType = when (it.selectedFreeContent) {
                    null,
                    FreeContent.UNRECOGNIZED,
                    FreeContent.FREE_CONTENT_MOVIE
                    -> FreeContentType.MOVIE

                    FreeContent.FREE_CONTENT_TV -> FreeContentType.TV
                },
                selectedPopularContentType = when (it.selectedPopularContent) {
                    null,
                    PopularContent.UNRECOGNIZED,
                    PopularContent.POPULAR_CONTENT_STREAMING
                    -> PopularContentType.STREAMING

                    PopularContent.POPULAR_CONTENT_THEATRES -> PopularContentType.IN_THEATRES
                    PopularContent.POPULAR_CONTENT_RENT -> PopularContentType.FOR_RENT
                },
                selectedTrendingContentTimeWindow = when (it.selectedTrendingContent) {
                    null,
                    TrendingContent.UNRECOGNIZED,
                    TrendingContent.TRENDING_CONTENT_DAY
                    -> TrendingContentTimeWindow.DAY

                    TrendingContent.TRENDING_CONTENT_WEEK -> TrendingContentTimeWindow.WEEK
                },
                accountDetails = it.accountDetails.asModel()
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
    }

    suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        userPreferences.updateData {
            it.copy { this.includeAdultResults = includeAdultResults }
        }
    }

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        userPreferences.updateData {
            it.copy {
                this.darkMode = when (selectedDarkMode) {
                    SelectedDarkMode.SYSTEM -> DarkMode.DARK_MODE_SYSTEM
                    SelectedDarkMode.DARK -> DarkMode.DARK_MODE_DARK
                    SelectedDarkMode.LIGHT -> DarkMode.DARK_MODE_LIGHT
                }
            }
        }
    }

    suspend fun setFreeContentPreference(selectedFreeContentType: FreeContentType) {
        userPreferences.updateData {
            it.copy {
                this.selectedFreeContent = when(selectedFreeContentType) {
                    FreeContentType.MOVIE -> FreeContent.FREE_CONTENT_MOVIE
                    FreeContentType.TV -> FreeContent.FREE_CONTENT_TV
                }
            }
        }
    }

    suspend fun setPopularContentPreference(selectedPopularContentType: PopularContentType) {
        userPreferences.updateData {
            it.copy {
                this.selectedPopularContent = when(selectedPopularContentType) {
                    PopularContentType.STREAMING -> PopularContent.POPULAR_CONTENT_STREAMING
                    PopularContentType.IN_THEATRES -> PopularContent.POPULAR_CONTENT_THEATRES
                    PopularContentType.FOR_RENT -> PopularContent.POPULAR_CONTENT_RENT
                }
            }
        }
    }

    suspend fun setTrendingContentPreference(
        selectedTrendingContentTimeWindow: TrendingContentTimeWindow
    ) {
        userPreferences.updateData {
            it.copy {
                this.selectedTrendingContent = when(selectedTrendingContentTimeWindow) {
                    TrendingContentTimeWindow.DAY -> TrendingContent.TRENDING_CONTENT_DAY
                    TrendingContentTimeWindow.WEEK -> TrendingContent.TRENDING_CONTENT_WEEK
                }
            }
        }
    }

    suspend fun saveAccountDetails(accountDetails: AccountDetails) {
        userPreferences.updateData {
            it.copy {
                this.accountDetails = this.accountDetails.copy {
                    id = accountDetails.id
                    name = accountDetails.name
                    username = accountDetails.username
                    gravatar = accountDetails.gravatar
                    avatar = accountDetails.avatar
                    includeAdult = accountDetails.includeAdult
                }
            }
        }
    }

    suspend fun removeAccountDetails() {
        userPreferences.updateData {
            it.copy { this.accountDetails = accountDetailsDefaultInstance }
        }
    }
}