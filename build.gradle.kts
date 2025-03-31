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

    // Firebase plugins
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.googleServices) apply false
}