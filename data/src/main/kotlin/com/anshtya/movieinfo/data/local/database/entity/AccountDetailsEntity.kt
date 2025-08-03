package com.anshtya.movieinfo.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.movieinfo.data.model.user.AccountDetails
import com.anshtya.movieinfo.data.network.model.auth.NetworkAccountDetails

@Entity(tableName = "account_details")
data class AccountDetailsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "gravatar_hash") val gravatarHash: String,
    @ColumnInfo(name = "include_adult") val includeAdult: Boolean,
    @ColumnInfo(name = "iso_639_1") val iso6391: String,
    @ColumnInfo(name = "iso_3166_1") val iso31661: String,
    val name: String,
    @ColumnInfo(name = "tmdb_avatar_path") val tmdbAvatarPath: String?,
    val username: String,
) {
    fun asModel() = AccountDetails(
        id = id,
        gravatar = gravatarHash,
        includeAdult = includeAdult,
        iso6391 = iso6391,
        iso31661 = iso31661,
        name = name,
        avatar = tmdbAvatarPath,
        username = username
    )
}

fun NetworkAccountDetails.asEntity() = AccountDetailsEntity(
    id = id,
    name = name,
    username = username,
    includeAdult = includeAdult,
    iso6391 = iso6391,
    iso31661 = iso31661,
    gravatarHash = avatar.gravatar.hash,
    tmdbAvatarPath = avatar.tmdb.avatarPath
)