package com.anshtya.movieinfo

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

private val Project.libs: VersionCatalog
        get() = extensions
                .getByType<VersionCatalogsExtension>()
                .named("libs")

internal fun Project.findLibrary(alias: String) = libs
        .findLibrary(alias)
        .get()