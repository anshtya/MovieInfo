import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.getLibrary

class AndroidHiltConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                add("implementation", getLibrary("hilt-android"))
                add("kapt", getLibrary("hilt-compiler"))
            }
        }
    }
}