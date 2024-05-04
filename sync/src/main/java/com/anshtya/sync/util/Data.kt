package com.anshtya.sync.util

import androidx.work.Data

fun <T: Enum<T>> Data.Builder.putEnum(key: String, taskType: T) = apply {
    putString(key, taskType.name.lowercase())
}

inline fun <reified T : Enum<T>> Data.getEnum(key: String) = enumValueOf<T>(getString(key)!!)