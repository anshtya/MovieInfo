package com.anshtya.core.model

data class UserData(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val isLoggedIn: Boolean,
    val darkMode: SelectedDarkMode,
    val selectedFreeContentType: FreeContentType,
    val selectedPopularContentType: PopularContentType,
    val selectedTrendingContentTimeWindow: TrendingContentTimeWindow,
    val accountDetails: AccountDetails
)
