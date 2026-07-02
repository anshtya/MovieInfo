package com.anshtya.movieinfo.core.network.model.details.tv

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkBroadcastNetwork(
    val name: String
)