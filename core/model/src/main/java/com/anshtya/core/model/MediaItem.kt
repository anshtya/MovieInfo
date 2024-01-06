package com.anshtya.core.model

import java.util.UUID

data class MediaItem(
    val id: String = UUID.randomUUID().toString(),
    val mediaId: Long,
    val imagePath: String,
    val name: String,
    val overview: String
)