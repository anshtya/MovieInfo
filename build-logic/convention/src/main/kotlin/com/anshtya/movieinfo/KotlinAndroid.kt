package com.anshtya.movieinfo

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    pluginManager.apply("org.jetbrains.kotlin.android")

    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(21)
    }
}