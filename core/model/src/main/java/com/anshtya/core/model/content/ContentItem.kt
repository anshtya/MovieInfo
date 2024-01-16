package com.anshtya.core.model.content

import java.util.UUID

data class ContentItem(
    val id: String = UUID.randomUUID().toString(),
    val mediaId: Int,
    val imagePath: String,
    val name: String
)