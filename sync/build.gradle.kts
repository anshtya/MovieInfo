plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.sync"
}

dependencies {
    implementation(projects.core.local)
    implementation(projects.core.network)
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    api(libs.androidx.hilt.work)
    api(libs.androidx.work.runtime.ktx)
    ksp(libs.hilt.compiler)
}