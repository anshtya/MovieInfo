package com.anshtya.core.model.user

import com.anshtya.core.model.SelectedDarkMode

data class UserData(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val isLoggedIn: Boolean,
    val darkMode: SelectedDarkMode,
    val accountDetails: AccountDetails
)
