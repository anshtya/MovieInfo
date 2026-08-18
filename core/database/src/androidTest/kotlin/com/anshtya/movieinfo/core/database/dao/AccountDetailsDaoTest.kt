package com.anshtya.movieinfo.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.anshtya.movieinfo.core.database.MovieInfoDatabase
import com.anshtya.movieinfo.core.database.entity.AccountDetailsEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AccountDetailsDaoTest {
    private lateinit var db: MovieInfoDatabase
    private lateinit var accountDetailsDao: AccountDetailsDao

    @Before
    fun setUp() {
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            testContext, MovieInfoDatabase::class.java
        ).build()
        accountDetailsDao = db.accountDetailsDao()
    }

    @Test
    fun accountDetailsDao_getAccountDetails_whenEmpty_returnsNull() = runTest {
        assertNull(accountDetailsDao.getAccountDetails())
    }

    @Test
    fun accountDetailsDao_addAccountDetails_isRetrieved() = runTest {
        val accountDetails = AccountDetailsEntity(
            id = 1,
            gravatarHash = "gravatarHash",
            includeAdult = false,
            iso6391 = "en",
            iso31661 = "US",
            name = "Test User",
            tmdbAvatarPath = null,
            username = "testuser"
        )

        accountDetailsDao.addAccountDetails(accountDetails)

        assertEquals(accountDetails, accountDetailsDao.getAccountDetails())
    }

    @Test
    fun accountDetailsDao_addAccountDetails_sameId_replacesExistingRow() = runTest {
        val original = AccountDetailsEntity(
            id = 1,
            gravatarHash = "gravatarHash1",
            includeAdult = false,
            iso6391 = "en",
            iso31661 = "US",
            name = "Original",
            tmdbAvatarPath = null,
            username = "original"
        )
        val replacement = original.copy(
            gravatarHash = "gravatarHash2",
            includeAdult = true,
            name = "Replacement",
            username = "replacement"
        )

        accountDetailsDao.addAccountDetails(original)
        accountDetailsDao.addAccountDetails(replacement)

        assertEquals(replacement, accountDetailsDao.getAccountDetails())
    }

    @Test
    fun accountDetailsDao_getRegionCode_returnsIso31661() = runTest {
        val accountDetails = AccountDetailsEntity(
            id = 1,
            gravatarHash = "gravatarHash",
            includeAdult = false,
            iso6391 = "en",
            iso31661 = "GB",
            name = "Test User",
            tmdbAvatarPath = null,
            username = "testuser"
        )

        accountDetailsDao.addAccountDetails(accountDetails)

        assertEquals("GB", accountDetailsDao.getRegionCode())
    }

    @Test
    fun accountDetailsDao_getRegionCode_whenEmpty_returnsNull() = runTest {
        assertNull(accountDetailsDao.getRegionCode())
    }

    @Test
    fun accountDetailsDao_deleteAccountDetails_removesRow() = runTest {
        val accountDetails = AccountDetailsEntity(
            id = 1,
            gravatarHash = "gravatarHash",
            includeAdult = false,
            iso6391 = "en",
            iso31661 = "US",
            name = "Test User",
            tmdbAvatarPath = null,
            username = "testuser"
        )
        accountDetailsDao.addAccountDetails(accountDetails)

        accountDetailsDao.deleteAccountDetails(accountDetails.id)

        assertNull(accountDetailsDao.getAccountDetails())
    }

    @After
    fun tearDown() {
        db.close()
    }
}
