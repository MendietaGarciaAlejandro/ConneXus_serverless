import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.1.0"  // Adjust version as needed
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop")

    /*
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
     */

    js(IR) {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

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

            // Solo para android
            // implementation(libs.multiplatform.settings.datastore.android)

            // Ktor
            //implementation(libs.ktor.client.okhttp)

            // Firebase Kotlin SDK
            implementation(project.dependencies.platform(libs.firebase.android.bom))

            // Firebase Kotlin SDK
            implementation(libs.firebase.database)
            implementation(libs.firebase.firestore)
            //implementation(libs.firebase.analytics)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.functions)
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.storage)
            //implementation(libs.firebase.installations)
            //implementation(libs.firebase.config)
            //implementation(libs.firebase.perf)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3) // Compose Material 3 (Nuevo)
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

            // Usar multplatform-settings en todas las plataformas
            // implementation(libs.multiplatform.settings)
            //implementation(libs.multiplatform.settings.no.arg)
            // implementation(libs.multiplatform.settings.datastore) // Opcional: Versión con DataStore

            // Ktor
            //implementation(libs.ktor.client.core)
            //implementation(libs.ktor.client.content.negotiation)
            //implementation(libs.ktor.serialization.kotlinx.json)

            // Ktor for JS
            //implementation(libs.ktor.client.js)

            // Firebase
            //implementation(libs.gitlive.firebase.kotlin.crashlytics)
            //implementation(libs.plugins.crashlytics)
            //implementation(libs.plugins.google.services)

            // Firebase Kotlin SDK
            implementation(libs.firebase.database)
            implementation(libs.firebase.firestore)
            //implementation(libs.firebase.analytics)
            implementation(libs.firebase.auth)
            //implementation(libs.firebase.functions)
            //implementation(libs.firebase.messaging)
            implementation(libs.firebase.storage)
            //implementation(libs.firebase.installations)
            //implementation(libs.firebase.config)
            //implementation(libs.firebase.perf)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.arkivanov.decompose.v080)
            //implementation(libs.decompose.extensions.compose.jetpack.v080)
            implementation(libs.decompose.jetbrains)

            // Solo para Desktop (JVM)
            // implementation(libs.multiplatform.settings.datastore)

            // Ktor
            //implementation(libs.ktor.client.cio.jvm)
            //implementation(libs.ktor.client.cio)

            // Firebase Kotlin SDK
            //implementation(libs.firebase.database)
            //implementation(libs.firebase.firestore)
            //implementation(libs.firebase.analytics)
            //implementation(libs.firebase.auth)
            //implementation(libs.firebase.functions)
            //implementation(libs.firebase.messaging)
            //implementation(libs.firebase.storage)
            //implementation(libs.firebase.installations)
            //implementation(libs.firebase.config)
            //implementation(libs.firebase.perf)
        }
        /*
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio) // JVM-specific Ktor client
                // Other JVM-specific dependencies
            }
        }
         */
        /*
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

                // Firebase Kotlin SDK
                //implementation(libs.firebase.database)
                //implementation(libs.firebase.firestore)
                //implementation(libs.firebase.analytics)
                //implementation(libs.firebase.auth)
                //implementation(libs.firebase.functions)
                //implementation(libs.firebase.messaging)
                //implementation(libs.firebase.storage)
                //implementation(libs.firebase.installations)
                //implementation(libs.firebase.config)
                //implementation(libs.firebase.perf)
            }
        }
         */

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                //implementation(libs.ktor.client.js) // Usa el cliente JS
                //implementation(libs.ktor.client.content.negotiation.v238)
                //implementation(libs.ktor.serialization.kotlinx.json.v238)
                implementation(npm("libphonenumber-js", "1.10.13"))

                // Firebase Kotlin SDK
                implementation(libs.firebase.database)
                implementation(libs.firebase.firestore)
                //implementation(libs.firebase.analytics)
                implementation(libs.firebase.auth)
                //implementation(libs.firebase.functions)
                //implementation(libs.firebase.messaging)
                implementation(libs.firebase.storage)
                //implementation(libs.firebase.installations)
                //implementation(libs.firebase.config)
                //implementation(libs.firebase.perf)
            }
        }

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
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.core.i18n)
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