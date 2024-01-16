package com.anshtya.core.model.user

import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.content.FreeContentType
import com.anshtya.core.model.content.PopularContentType
import com.anshtya.core.model.content.TrendingContentTimeWindow

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
