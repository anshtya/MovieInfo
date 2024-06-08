import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.getLibrary

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
                dependencies {
                    val bom = getLibrary("compose-bom")
                    add("implementation", platform(bom))
                    add("implementation", getLibrary("material3"))
                    add("implementation", getLibrary("ui-tooling-preview"))
                    add("implementation", getLibrary("androidx-lifecycle-runtime-compose"))
                    add("implementation", getLibrary("androidx-navigation-compose"))

                    add("androidTestImplementation", platform(bom))
                }
            }
        }
    }
}