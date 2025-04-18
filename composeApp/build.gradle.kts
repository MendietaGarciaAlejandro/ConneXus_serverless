import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    // id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    jvm("desktop")
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

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.arkivanov.decompose.v080)
            implementation(libs.decompose.extensions.compose.jetpack.v080)
            implementation(libs.kotlinx.serialization)

            // Dependencies for Firebase
//            implementation(project.dependencies.platform(libs.firebase.android.bom))
//            implementation(libs.firebase.database)
//            implementation(libs.firebase.firestore)
//            implementation(libs.firebase.auth)
//            implementation(libs.firebase.functions)
//            implementation(libs.firebase.messaging)
//            implementation(libs.firebase.storage)

            // Dependencies for Supabase
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")

            // Dependencias de Ktor
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Dependencias encriptación
            implementation(libs.cryptography.provider.jdk)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization)

            // Dependencies for Firebase
//            implementation(libs.firebase.database)
//            implementation(libs.firebase.firestore)
//            implementation(libs.firebase.auth)
//            implementation(libs.firebase.functions)
//            implementation(libs.firebase.messaging)
//            implementation(libs.firebase.storage)

            // Dependencies for Supabase
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")


            // Dependencias de Ktor
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Dependencias encriptación
            implementation(libs.cryptography.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.arkivanov.decompose.v080)
            implementation(libs.decompose.jetbrains)

            // Dependencies for Supabase
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")


            // Dependencias de Ktor
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Dependencias encriptación
            implementation(libs.cryptography.provider.jdk)
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)

                // Dependencies for Firebase
//                implementation(npm("libphonenumber-js", "1.10.13"))
//                implementation(libs.firebase.database)
//                implementation(libs.firebase.firestore)
//                implementation(libs.firebase.auth)
//                implementation(libs.firebase.storage)
            }
        }
        wasmJsMain.dependencies {
            // Dependencies for Supabase
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)

            // Dependencias de Ktor
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.js)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Dependencias encriptación
            implementation(libs.cryptography.provider.webcrypto)
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
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.core.i18n)

    // Dependencies for Firebase
//    implementation(platform(libs.firebase.android.bom))
//    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.firestore.ktx)

    implementation(libs.androidx.ui.android)
    implementation(libs.material3.android)
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