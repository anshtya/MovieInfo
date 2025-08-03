package com.anshtya.movieinfo.core.local.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserPreferencesDataStoreTest {
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setUp() {
        userPreferencesDataStore = UserPreferencesDataStore(
            PreferenceDataStoreFactory.create {
                tmpFolder.newFile("user_prefs_test.preferences_pb")
            }
        )
    }

    @Test
    fun hideOnboardingDefaultIsFalse() = runTest {
        TestCase.assertEquals(
            userPreferencesDataStore.userData.first().hideOnboarding,
            false
        )
    }

    @Test
    fun hideOnboardingWhenSetTrue() = runTest {
        userPreferencesDataStore.setHideOnboarding(true)
        TestCase.assertEquals(
            userPreferencesDataStore.userData.first().hideOnboarding,
            true
        )
    }

    @Test
    fun darkModeDefaultIsSystem() = runTest {
        TestCase.assertEquals(
            userPreferencesDataStore.userData.first().darkMode,
            SelectedDarkMode.SYSTEM
        )
    }
}