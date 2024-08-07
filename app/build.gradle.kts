plugins {
    id("movieinfo.android.application")
    id("movieinfo.android.application.compose")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo"

    defaultConfig {
        applicationId = "com.anshtya.movieinfo"
        versionCode = 6
        versionName = "2.2.1"

        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
        create("demoRelease") {
            initWith(buildTypes.getByName("release"))
            applicationIdSuffix = ".demoRelease"
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.data)
    implementation(projects.feature.auth)
    implementation(projects.feature.details)
    implementation(projects.feature.movies)
    implementation(projects.feature.search)
    implementation(projects.feature.tv)
    implementation(projects.feature.you)
    implementation(projects.sync)

    implementation(libs.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.kt.compose)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit)
}