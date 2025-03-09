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