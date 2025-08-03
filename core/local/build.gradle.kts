plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
    alias(libs.plugins.room)
}

android {
    namespace = "com.anshtya.movieinfo.core.local"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}