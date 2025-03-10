package org.connexuss.project

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.connexuss.project.navegacion.Navegacion
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Composable
@Preview
fun App() {
    val todosLosColores = ListaCompletaColores
    var temaClaro by rememberSaveable { mutableStateOf(true) }
    // Define los colores que se usarán en la aplicación
    var colores = if (temaClaro) coloresClaros else coloresOscuros
    var colorPillado: Any? = null
    if (colorPillado != null) {
        colores = ListaCompletaColores[colorPillado as Int] as Colors
    }
    MaterialTheme(colors = colores) {
        // Aquí usas tu NavHost para la navegación
        colorPillado =
        Navegacion(
            temaClaro = temaClaro,
            onToggleTheme = { temaClaro = !temaClaro },
            colores = todosLosColores,
        )
    }
}
/*
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