plugins {
    // Android plugins
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false

    // Compose plugins
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false

    // Kotlin plugins
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization)

    // Alias de los plugins de Firebase
//    alias(libs.plugins.google.services) apply false
//    alias(libs.plugins.crashlytics) apply false

    // Alias de los plugins de Firebase (Kotlin SDK)
    //id("com.google.gms.google-services") version "4.4.2" apply false
}