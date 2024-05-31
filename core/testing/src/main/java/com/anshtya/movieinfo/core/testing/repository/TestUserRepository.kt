package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import com.anshtya.movieinfo.core.model.user.AccountDetails
import com.anshtya.movieinfo.core.model.user.UserData
import com.anshtya.movieinfo.data.repository.UserRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

val emptyUserData = UserData(
    useDynamicColor = false,
    includeAdultResults = false,
    darkMode = SelectedDarkMode.SYSTEM,
    hideOnboarding = false
)

val accountDetails = AccountDetails(
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
    private val _userData = MutableSharedFlow<UserData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val userData: Flow<UserData> = _userData.asSharedFlow()

    private fun getCurrentUserData() = _userData.replayCache.firstOrNull() ?: emptyUserData

    private val _accountDetails = MutableSharedFlow<AccountDetails?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val accountDetails: Flow<AccountDetails?> = _accountDetails.asSharedFlow()

    private var userSessionId: String? = "id"

    override fun isSignedIn(): Boolean = userSessionId != null

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        _userData.tryEmit(
            getCurrentUserData().copy(useDynamicColor = useDynamicColor)
        )
    }

    override suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        _userData.tryEmit(
            getCurrentUserData().copy(includeAdultResults = includeAdultResults)
        )
    }

    override suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        _userData.tryEmit(
            getCurrentUserData().copy(darkMode = selectedDarkMode)
        )
    }

    override suspend fun updateAccountDetails(accountId: Int): NetworkResponse<Unit> {
        return if (accountId == 0) {
            NetworkResponse.Error("error")
        } else {
            NetworkResponse.Success(Unit)
        }
    }

    override suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        _userData.tryEmit(
            getCurrentUserData().copy(hideOnboarding = hideOnboarding)
        )
    }

    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }

    fun setAccountDetails(accountDetails: AccountDetails) {
        _accountDetails.tryEmit(accountDetails)
    }

    fun setUserSessionId(id: String?) {
        userSessionId = id
    }
}