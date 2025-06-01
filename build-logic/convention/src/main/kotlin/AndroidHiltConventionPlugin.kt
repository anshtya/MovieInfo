import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.anshtya.movieinfo.findLibrary

class AndroidHiltConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                add("implementation", findLibrary("dagger-hilt-android"))
                add("ksp", findLibrary("dagger-hilt-compiler"))
            }
        }
    }
}