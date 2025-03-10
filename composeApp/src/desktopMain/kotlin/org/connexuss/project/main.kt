package org.connexuss.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ConneXus_serverless",
    ) {
        App()
    }
}

actual fun createHttpClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}