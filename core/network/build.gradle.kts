import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL") ?: ""
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        val accessToken = gradleLocalProperties(rootDir, providers)
            .getProperty("ACCESS_TOKEN") ?: ""
        buildConfigField("String", "ACCESS_TOKEN", "\"$accessToken\"")
    }
}

dependencies {
    api(projects.core.model)

    api(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging.interceptor)

    testImplementation(projects.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}