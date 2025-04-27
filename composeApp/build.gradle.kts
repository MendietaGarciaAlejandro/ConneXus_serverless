import org.jetbrains.compose.ExperimentalComposeLibrary
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
    //kotlin("plugin.serialization") version "2.1.0"
    alias(libs.plugins.kotlinxSerialization)
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
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

        commonMain.dependencies {
            // Compose dependencies
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // AndroidX & Kotlin dependencies
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization)

            // Ktor dependencies
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Supabase dependencies
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            //implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")

            // Cryptography dependencies
            implementation(libs.cryptography.core)

            // Firebase dependencies (commented)
//            implementation(libs.firebase.database)
//            implementation(libs.firebase.firestore)
//            implementation(libs.firebase.auth)
//            implementation(libs.firebase.functions)
//            implementation(libs.firebase.messaging)
//            implementation(libs.firebase.storage)
        }

        androidMain.dependencies {
            // Compose & AndroidX dependencies
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.arkivanov.decompose.v080)
            implementation(libs.decompose.extensions.compose.jetpack.v080)
            implementation(libs.kotlinx.serialization)

            // Ktor dependencies
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Supabase dependencies
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            //implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")

            // Cryptography dependencies
            implementation(libs.cryptography.provider.jdk)

            // Firebase dependencies (commented)
//            implementation(project.dependencies.platform(libs.firebase.android.bom))
//            implementation(libs.firebase.database)
//            implementation(libs.firebase.firestore)
//            implementation(libs.firebase.auth)
//            implementation(libs.firebase.functions)
//            implementation(libs.firebase.messaging)
//            implementation(libs.firebase.storage)
        }

        desktopMain.dependencies {
            // Compose & Kotlin dependencies
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.arkivanov.decompose.v080)
            implementation(libs.decompose.jetbrains)

            // Ktor dependencies
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Supabase dependencies
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            //implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")

            // Cryptography dependencies
            implementation(libs.cryptography.provider.jdk)
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)

                // Firebase dependencies (commented)
//                implementation(npm("libphonenumber-js", "1.10.13"))
//                implementation(libs.firebase.database)
//                implementation(libs.firebase.firestore)
//                implementation(libs.firebase.auth)
//                implementation(libs.firebase.storage)
            }
        }

        wasmJsMain.dependencies {
            // Supabase dependencies
            //implementation(libs.bom)
            implementation(libs.supabase.kt)
            implementation(libs.storage.kt)
            implementation(libs.supabase.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            //implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.1")

            // Ktor dependencies
            implementation(libs.ktorClientCore)
            implementation(libs.ktor.client.js)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Cryptography dependencies
            implementation(libs.cryptography.provider.webcrypto)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.annotations.common)
            implementation(libs.assertk)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.core.i18n)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization)

    // Firebase dependencies (commented)
//    implementation(platform(libs.firebase.android.bom))
//    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.firestore.ktx)
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