package com.anshtya.movieinfo.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
)