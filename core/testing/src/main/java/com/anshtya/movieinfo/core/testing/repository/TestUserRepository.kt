package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import com.anshtya.movieinfo.core.model.user.AccountDetails
import com.anshtya.movieinfo.core.model.user.UserData
import com.anshtya.movieinfo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val testUserData = UserData(
    useDynamicColor = false,
    includeAdultResults = false,
    darkMode = SelectedDarkMode.SYSTEM,
    hideOnboarding = false
)

val testAccountDetails = AccountDetails(
    avatar = "avatar",
    gravatar = "gravatar",
    id = 0,
    includeAdult = false,
    iso6391 = "iso63",
    iso31661 = "iso31",
    name = "name",
    username = "username"
)

class TestUserRepository: UserRepository {
    private var generateError = false

    private val _userData = MutableStateFlow(testUserData)


    override val userData = _userData.asStateFlow()

    override suspend fun getAccountDetails(): AccountDetails = testAccountDetails

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        _userData.update {
            it.copy(useDynamicColor = useDynamicColor)
        }
    }

    override suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        _userData.update {
            it.copy(includeAdultResults = includeAdultResults)
        }
    }

    override suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        _userData.update {
            it.copy(darkMode = selectedDarkMode)
        }
    }

    override suspend fun updateAccountDetails(accountId: Int): NetworkResponse<Unit> {
        return if (generateError) {
            NetworkResponse.Error()
        } else {
            NetworkResponse.Success(Unit)
        }
    }

    override suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        _userData.update {
            it.copy(hideOnboarding = hideOnboarding)
        }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}