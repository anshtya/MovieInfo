plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("compose") {
            id = "movieinfo.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "movieinfo.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "movieinfo.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}