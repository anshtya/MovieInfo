package com.anshtya.core.network.model.details

import com.squareup.moshi.Json

data class NetworkProductionCompany(
    val id: Int,
    @field:Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @field:Json(name = "origin_country") val originCountry: String
)