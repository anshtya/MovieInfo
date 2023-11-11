import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension>() {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.4.3"
                }
                dependencies {
                    val bom = deps.findLibrary("compose-bom").get()
                    add("implementation", platform(bom))
                    add("implementation", deps.findLibrary("material3").get())
                    add("implementation", deps.findLibrary("ui-tooling-preview").get())
                    add("implementation", deps.findLibrary("androidx-lifecycle-runtime-compose").get())
                }
            }
        }
    }
}