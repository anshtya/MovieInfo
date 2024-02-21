import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val baseUrl = gradleLocalProperties(rootDir).getProperty("BASE_URL") ?: ""
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        val accessToken = gradleLocalProperties(rootDir).getProperty("ACCESS_TOKEN") ?: ""
        buildConfigField("String", "ACCESS_TOKEN", "\"$accessToken\"")
    }
}

dependencies {
    api(projects.core.model)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    api(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.mockwebserver)
}