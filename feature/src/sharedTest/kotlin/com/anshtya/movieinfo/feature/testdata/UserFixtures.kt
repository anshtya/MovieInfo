package com.anshtya.movieinfo.feature.testdata

import com.anshtya.movieinfo.data.model.SelectedDarkMode
import com.anshtya.movieinfo.data.model.user.AccountDetails
import com.anshtya.movieinfo.data.model.user.UserData

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
