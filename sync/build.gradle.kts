plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.sync"
}

dependencies {
    implementation(projects.data)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.hilt.compiler)
}