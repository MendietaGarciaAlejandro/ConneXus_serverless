package org.connexuss.project
/*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

// Declaración "expect" para crear el cliente HTTP en cada plataforma
expect fun createHttpClient(): HttpClient

// Función compartida para hacer una petición GET a una URL (por ejemplo, a la API REST de Firebase)
suspend fun fetchFirebaseData(url: String): String {
    val client = createHttpClient()
    // Realiza una solicitud GET y obtiene el cuerpo como texto
    val response: HttpResponse = client.get(url)
    val text = response.bodyAsText()
    client.close()
    return text
}
 */