package com.anshtya.movieinfo.core.network.model.details

import com.squareup.moshi.Json

data class NetworkProductionCompany(
    val id: Int,
    @Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @Json(name = "origin_country") val originCountry: String
)