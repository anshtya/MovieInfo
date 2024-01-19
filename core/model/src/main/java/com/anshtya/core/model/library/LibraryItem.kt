package com.anshtya.core.model.library

data class LibraryItem(
    val id: Int = 0,
    val imagePath: String,
    val name: String,
    val mediaId: Int,
    val mediaType: String
)