package com.anshtya.core.ui

import android.content.Context
import androidx.annotation.StringRes

sealed interface ErrorText {
    data class SimpleText(val text: String): ErrorText
    data class StringResource(@StringRes val id: Int): ErrorText

    fun toText(context: Context): String {
        return when(this) {
            is SimpleText -> text
            is StringResource -> context.getString(id)
        }
    }
}