package org.connexuss.project.interfaces.colores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.idiomas.traducir
import org.connexuss.project.interfaces.modificadorTamannio.LimitaTamanioAncho
import org.connexuss.project.interfaces.sistema.DefaultTopBar

fun seleccionarTema(light: Colors, dark: Colors, temaClaro: Boolean): Colors =
    if (temaClaro) light else dark

data class TemaConfig(
    val temaClaro: Boolean = true,
    val colorTemaKey: String = "claro" // "claro" representa el tema predeterminado (coloresClaros)
)

fun getColorsForTheme(temaClaro: Boolean, colorTemaKey: String): androidx.compose.material.Colors {
    return when (colorTemaKey) {
        "azul" -> if (temaClaro) coloresAzulClaro else coloresAzulOscuro
        "amarillo" -> if (temaClaro) coloresAmarilloClaro else coloresAmarilloOscuro
        "verde" -> if (temaClaro) coloresVerdeClaro else coloresVerdeOscuro
        "rojo" -> if (temaClaro) coloresRojoClaro else coloresRojoOscuro
        "morado" -> if (temaClaro) coloresMoradoClaro else coloresMoradoOscuro
        "gris" -> if (temaClaro) coloresGrisClaro else coloresGrisOscuro
        "naranja" -> if (temaClaro) coloresNaranjaClaro else coloresNaranjaOscuro
        else -> if (temaClaro) coloresClaros else coloresOscuros
    }
}

/*
val TemaConfigSaver: Saver<TemaConfig, List<Any>> = listSaver<TemaConfig, Any>(
    save = { config: TemaConfig ->
        listOf<Any>(config.temaClaro, config.colorTemaKey)
    },
    restore = { list: List<Any> ->
        TemaConfig(
            temaClaro = list[0] as Boolean,
            colorTemaKey = list[1] as String
        )
    }
) as Saver<TemaConfig, List<Any>>
 */

val TemaConfigSaver: Saver<TemaConfig, List<Any>> = Saver(
    save = { config -> listOf(config.temaClaro, config.colorTemaKey) },
    restore = { list ->
        TemaConfig(
            temaClaro = list[0] as Boolean,
            colorTemaKey = list[1] as String
        )
    }
)

@Composable
fun PantallaCambiarTema(
    navController: NavHostController,
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit  // Espera una clave (String)
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes_tema"),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = false,
                irParaAtras = true
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        traducir("modo"),
                        style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(traducir("modo_claro"), style = MaterialTheme.typography.body1)
                        val isDarkMode = !temaConfig.temaClaro
                        Switch(checked = isDarkMode, onCheckedChange = { onToggleTheme() })
                        Text(traducir("modo_oscuro"), style = MaterialTheme.typography.body1)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        traducir("colores"),
                        style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { onColorChange("azul") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_azul"))
                    }
                    Button(
                        onClick = { onColorChange("amarillo") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_amarillo"))
                    }
                    Button(
                        onClick = { onColorChange("verde") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_verde"))
                    }
                    Button(
                        onClick = { onColorChange("rojo") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_rojo"))
                    }
                    Button(
                        onClick = { onColorChange("morado") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_morado"))
                    }
                    Button(
                        onClick = { onColorChange("gris") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_gris"))
                    }
                    Button(
                        onClick = { onColorChange("naranja") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("cambiar_a_tema_naranja"))
                    }
                }
            }
        }
    }
}

val coloresClaros = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresOscuros = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Azul ---
val coloresAzulClaro = lightColors(
    primary = Color(0xFF2196F3),         // Azul
    primaryVariant = Color(0xFF1976D2),
    secondary = Color(0xFF64B5F6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFF44336),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresAzulOscuro = darkColors(
    primary = Color(0xFF2196F3),
    primaryVariant = Color(0xFF1976D2),
    secondary = Color(0xFF64B5F6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFF44336),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Amarilla ---
val coloresAmarilloClaro = lightColors(
    primary = Color(0xFFFFEB3B),         // Amarillo
    primaryVariant = Color(0xFFFBC02D),
    secondary = Color(0xFFFFF176),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.Black,             // Texto oscuro sobre amarillo
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresAmarilloOscuro = darkColors(
    primary = Color(0xFFFFEB3B),
    primaryVariant = Color(0xFFFBC02D),
    secondary = Color(0xFFFFF176),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Verde ---
val coloresVerdeClaro = lightColors(
    primary = Color(0xFF4CAF50),         // Verde
    primaryVariant = Color(0xFF388E3C),
    secondary = Color(0xFF81C784),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFF44336),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresVerdeOscuro = darkColors(
    primary = Color(0xFF4CAF50),
    primaryVariant = Color(0xFF388E3C),
    secondary = Color(0xFF81C784),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFF44336),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Roja ---
val coloresRojoClaro = lightColors(
    primary = Color(0xFFF44336),         // Rojo
    primaryVariant = Color(0xFFD32F2F),
    secondary = Color(0xFFEF5350),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresRojoOscuro = darkColors(
    primary = Color(0xFFF44336),
    primaryVariant = Color(0xFFD32F2F),
    secondary = Color(0xFFEF5350),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFB00020),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Morada ---
val coloresMoradoClaro = lightColors(
    primary = Color(0xFF9C27B0),         // Morado
    primaryVariant = Color(0xFF7B1FA2),
    secondary = Color(0xFFE1BEE7),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresMoradoOscuro = darkColors(
    primary = Color(0xFF9C27B0),
    primaryVariant = Color(0xFF7B1FA2),
    secondary = Color(0xFFE1BEE7),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFD32F2F),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Gris ---
val coloresGrisClaro = lightColors(
    primary = Color(0xFF9E9E9E),         // Gris
    primaryVariant = Color(0xFF616161),
    secondary = Color(0xFFBDBDBD),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresGrisOscuro = darkColors(
    primary = Color(0xFF9E9E9E),
    primaryVariant = Color(0xFF616161),
    secondary = Color(0xFFBDBDBD),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFD32F2F),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// --- Paleta Naranja ---
val coloresNaranjaClaro = lightColors(
    primary = Color(0xFFFF9800),         // Naranja
    primaryVariant = Color(0xFFF57C00),
    secondary = Color(0xFFFFB74D),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val coloresNaranjaOscuro = darkColors(
    primary = Color(0xFFFF9800),
    primaryVariant = Color(0xFFF57C00),
    secondary = Color(0xFFFFB74D),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFD32F2F),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)