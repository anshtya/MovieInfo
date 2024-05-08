package com.anshtya.movieinfo.core.model.user

import com.anshtya.movieinfo.core.model.SelectedDarkMode

data class UserData(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode,
    val hideOnboarding: Boolean
)
