package com.anshtya.movieinfo.data.model.details.tv

import com.anshtya.movieinfo.core.network.model.details.tv.NetworkCreatedBy

data class CreatedBy(
    val id: Int,
    val name: String
)

fun NetworkCreatedBy.asModel() = CreatedBy(
    id = id,
    name = name
)