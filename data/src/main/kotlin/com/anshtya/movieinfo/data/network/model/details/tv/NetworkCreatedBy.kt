package com.anshtya.movieinfo.data.network.model.details.tv

import com.anshtya.movieinfo.data.model.details.tv.CreatedBy
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCreatedBy(
    val id: Int,
    val name: String
) {
    fun asModel() = CreatedBy(
        id = id,
        name = name
    )
}
