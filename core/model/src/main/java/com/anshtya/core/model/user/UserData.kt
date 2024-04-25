package com.anshtya.core.model.user

import com.anshtya.core.model.SelectedDarkMode

data class UserData(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode,
    val hideOnboarding: Boolean
)
