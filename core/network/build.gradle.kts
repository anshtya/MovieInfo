import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.anshtya.movieinfo.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
        val accessToken = gradleLocalProperties(rootDir, providers)
            .getProperty("ACCESS_TOKEN") ?: ""
        buildConfigField("String", "ACCESS_TOKEN", "\"$accessToken\"")
    }

}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
}