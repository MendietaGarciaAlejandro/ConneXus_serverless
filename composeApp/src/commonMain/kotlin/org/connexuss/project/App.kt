package org.connexuss.project

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.connexuss.project.navegacion.Navegacion

data class TemaConfig(
    val temaClaro: Boolean = true,
    val colores: Colors = coloresClaros
)

@Composable
fun App() {
    var temaConfig by rememberSaveable { mutableStateOf(TemaConfig()) }
    MaterialTheme(colors = temaConfig.colores) {
        Navegacion(
            temaConfig = temaConfig,
            onToggleTheme = { temaConfig = temaConfig.copy(temaClaro = !temaConfig.temaClaro,
                colores = if (temaConfig.temaClaro) coloresOscuros else coloresClaros) },
            onColorChange = { nuevoColor ->
                temaConfig = temaConfig.copy(colores = nuevoColor)
            }
        )
    }
}