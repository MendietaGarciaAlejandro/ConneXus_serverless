rootProject.name = "ConneXus_serverless"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven("https://repo.repsy.io/mvn/arkivanov/public") // Repositorio de Arkivanov
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven{ url = uri("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") }
        maven("https://repo.repsy.io/mvn/arkivanov/public") // Repositorio de Arkivanov
    }
}

include(":composeApp")
