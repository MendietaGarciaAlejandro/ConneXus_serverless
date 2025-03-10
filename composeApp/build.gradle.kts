import com.android.kotlin.multiplatform.ide.models.serialization.androidTargetKey
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    /*
    js(IR) {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
     */

    sourceSets {
        val desktopMain by getting
        // val wasmJsMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.arkivanov.decompose.v080)
            implementation(libs.decompose.extensions.compose.jetpack.v080)
            //implementation(libs.decompose)
            implementation(libs.kotlinx.serialization)

            // Ktor
            //implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.navigation.compose)
            implementation(libs.navigation.compose)
            //implementation(libs.decompose.jetbrains)
            //implementation(libs.decompose)
            implementation(libs.kotlinx.serialization)

            // Ktor
            //implementation(libs.ktor.client.core)
            //implementation(libs.ktor.client.content.negotiation)
            //implementation(libs.ktor.serialization.kotlinx.json)

            // Ktor for JS
            //implementation(libs.ktor.client.js)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.arkivanov.decompose.v080)
            //implementation(libs.decompose.extensions.compose.jetpack.v080)
            implementation(libs.decompose.jetbrains)

            // Ktor
            //implementation(libs.ktor.client.cio.jvm)
            //implementation(libs.ktor.client.cio)
        }
        /*
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio) // JVM-specific Ktor client
                // Other JVM-specific dependencies
            }
        }
         */
        val wasmJsMain by getting {
            dependencies {
                //implementation("org.jetbrains.compose.web:compose-web-router:1.0.0-beta6")
                implementation(libs.kotlinx.serialization)

                // Ktor
                //implementation(libs.ktor.client.js)
                //implementation(libs.ktor.client.content.negotiation.v238) // Faltaba
                //implementation(libs.ktor.client.core.v238) // ¡Versión 2.3.8+!
                //implementation(libs.ktor.serialization.kotlinx.json) // Faltaba
                //implementation(libs.ktor.serialization.kotlinx.json.v238) // Requerido
            }
        }
        /*
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js) // Usa el cliente JS
                implementation(libs.ktor.client.content.negotiation.v238)
                implementation(libs.ktor.serialization.kotlinx.json.v238)
            }
        }
         */
    }
}

android {
    namespace = "org.connexuss.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.connexuss.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.animation.core.lint)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.connexuss.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.connexuss.project"
            packageVersion = "1.0.0"
        }
    }
}
