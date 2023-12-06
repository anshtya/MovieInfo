package com.anshtya.data.model

import com.anshtya.network.model.NetworkStreamingItem

data class StreamingItem(
    val id: Int,
    val posterPath: String
)

fun NetworkStreamingItem.asModel() = StreamingItem(
    id = id,
    posterPath = posterPath
)