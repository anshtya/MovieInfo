import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("kapt")
    id("movieinfo.android.library")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.anshtya.network"

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

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.mockwebserver)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}