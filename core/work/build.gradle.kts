plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.core.work"
}

dependencies {
    implementation(projects.core.data)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.hilt.compiler)
}