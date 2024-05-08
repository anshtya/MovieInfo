package com.anshtya.movieinfo.core.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshtya.movieinfo.core.local.database.entity.AccountDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDetailsDao {
    @Query("SELECT * FROM account_details LIMIT 1")
    fun getAccountDetails(): Flow<AccountDetailsEntity?>

    @Query("SELECT iso_3166_1 FROM account_details LIMIT 1")
    suspend fun getRegionCode(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccountDetails(accountDetails: AccountDetailsEntity)

    @Query("DELETE FROM account_details WHERE id = :accountId")
    suspend fun deleteAccountDetails(accountId: Int)
}