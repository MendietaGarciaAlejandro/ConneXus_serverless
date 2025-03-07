package org.connexuss.project

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import org.connexuss.project.navegacion.Navegacion
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var temaClaro by rememberSaveable { mutableStateOf(true) }

    // Define las paletas de colores
    val coloresClaros = lightColors(
        primary = Color(0xFF6200EE),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        error = Color(0xFFB00020),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFF000000),
        onBackground = Color(0xFF000000),
        onSurface = Color(0xFF000000),
        onError = Color(0xFFFFFFFF)
    )

    val coloresOscuros = darkColors(
        primary = Color(0xFFBB86FC),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212),
        surface = Color(0xFF121212),
        error = Color(0xFFCF6679),
        onPrimary = Color(0xFF000000),
        onSecondary = Color(0xFF000000),
        onBackground = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        onError = Color(0xFF000000)
    )

    val colores = if (temaClaro) coloresClaros else coloresOscuros

    MaterialTheme(colors = colores) {
        // Aquí usas tu NavHost para la navegación
        Navegacion(
            temaClaro = temaClaro,
            onToggleTheme = { temaClaro = !temaClaro }
        )
    }
}