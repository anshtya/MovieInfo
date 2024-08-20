package com.anshtya.movieinfo

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*,*,*,*,*,*>
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = findLibrary("compose-bom")
            add("implementation", platform(bom))
            add("implementation", findLibrary("material3"))
            add("implementation", findLibrary("ui-tooling-preview"))

            add("androidTestImplementation", platform(bom))
        }
    }
}