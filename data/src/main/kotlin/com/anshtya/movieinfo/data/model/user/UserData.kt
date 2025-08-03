package com.anshtya.movieinfo.data.model.user

import com.anshtya.movieinfo.data.model.SelectedDarkMode

data class UserData(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode,
    val hideOnboarding: Boolean
)
