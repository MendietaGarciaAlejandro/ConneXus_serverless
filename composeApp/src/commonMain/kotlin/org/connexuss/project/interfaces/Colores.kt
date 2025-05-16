package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.launch
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.getTemaConfigFlow
import org.connexuss.project.persistencia.setTemaConfig

/**
 * Selecciona el esquema de colores en función del parámetro temaClaro.
 *
 * @param light Esquema de colores claro.
 * @param dark Esquema de colores oscuro.
 * @param temaClaro Bandera que indica si se debe utilizar el tema claro.
 * @return El esquema de colores seleccionado.
 */
fun seleccionarTema(light: Colors, dark: Colors, temaClaro: Boolean): Colors =
    if (temaClaro) light else dark

/**
 * Configuración del tema de la aplicación.
 *
 * @property temaClaro Indica si se utiliza el tema claro.
 * @property colorTemaKey Clave que representa el conjunto de colores a utilizar.
 */
data class TemaConfig(
    val temaClaro: Boolean = true,
    val colorTemaKey: String = "claro" // "claro" representa el tema predeterminado (coloresClaros)
)

/**
 * Obtiene el esquema de colores correspondiente según la configuración del tema.
 *
 * @param temaClaro Indica si se utiliza el tema claro.
 * @param colorTemaKey Clave que representa el conjunto de colores a utilizar.
 * @return Esquema de colores seleccionado.
 */
fun getColorsForTheme(temaClaro: Boolean, colorTemaKey: String): ColorScheme {
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

/**
 * Saver para almacenar y restaurar la configuración del tema.
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

@OptIn(ExperimentalSettingsApi::class)
@Composable
fun AppThemeWrapper(settingsState: SettingsState, content: @Composable () -> Unit) {
    // 1) Recoge TemaConfig como State
    val temaConfig by settingsState
        .getTemaConfigFlow()
        .collectAsState(initial = TemaConfig())

    // 2) Extrae Boolean y String
    val isLight = temaConfig.temaClaro
    val colorKey = temaConfig.colorTemaKey

    val colorScheme = getColorsForTheme(isLight, colorKey)

    val shapes      = Shapes(
        small = MaterialTheme.shapes.small,
        medium = MaterialTheme.shapes.medium,
        large = MaterialTheme.shapes.large,
        extraSmall = MaterialTheme.shapes.extraSmall,
        extraLarge = MaterialTheme.shapes.extraLarge
    )

    // 3) Pásalos a getColorsForTheme
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = Typography(),
    ) {
        content()
    }
}

/**
 * Pantalla para cambiar el tema de la aplicación.
 *
 * @param navController Controlador de navegación.
 * @param temaConfig Configuración actual del tema.
 * @param onToggleTheme Función callback para alternar entre tema claro y oscuro.
 * @param onColorChange Función callback que espera una clave de color para cambiar el tema.
 */
@Composable
fun PantallaCambiarTema(
    navController: NavHostController,
    settingsState: SettingsState
) {
    val scope = rememberCoroutineScope()
    val temaConfig by settingsState.getTemaConfigFlow().collectAsState(initial = TemaConfig())

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("cambiar_tema"),
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false
            )
        }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Switch claro/oscuro
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(traducir("modo_claro"))
                    Switch(
                        checked = !temaConfig.temaClaro,
                        onCheckedChange = {
                            scope.launch {
                                settingsState.setTemaConfig(
                                    temaConfig.copy(temaClaro = !temaConfig.temaClaro)
                                )
                            }
                        }
                    )
                    Text(traducir("modo_oscuro"))
                }
                Spacer(Modifier.height(16.dp))
                // Botones de color
                listOf(
                    "azul",
                    "amarillo",
                    "verde",
                    "rojo",
                    "morado",
                    "gris",
                    "naranja"
                ).forEach { keyColor ->
                    Button(
                        onClick = {
                            scope.launch {
                                settingsState.setTemaConfig(
                                    temaConfig.copy(colorTemaKey = keyColor)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("tema_$keyColor"))
                    }
                }
            }
        }
    }
}

val coloresClaros = lightColorScheme(
    primary = Color(0xFF6200EE),
    primaryContainer = Color(0xFF3700B3),
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

val coloresOscuros = darkColorScheme(
    primary = Color(0xFFBB86FC),
    primaryContainer = Color(0xFF3700B3),
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
val coloresAzulClaro = lightColorScheme(
    primary = Color(0xFF2196F3),         // Azul
    primaryContainer = Color(0xFF1976D2),
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

val coloresAzulOscuro = darkColorScheme(
    primary = Color(0xFF2196F3),
    primaryContainer = Color(0xFF1976D2),
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
val coloresAmarilloClaro = lightColorScheme(
    primary = Color(0xFFFFEB3B),         // Amarillo
    primaryContainer = Color(0xFFFBC02D),
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

val coloresAmarilloOscuro = darkColorScheme(
    primary = Color(0xFFFFEB3B),
    primaryContainer = Color(0xFFFBC02D),
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
val coloresVerdeClaro = lightColorScheme(
    primary = Color(0xFF4CAF50),         // Verde
    primaryContainer = Color(0xFF388E3C),
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

val coloresVerdeOscuro = darkColorScheme(
    primary = Color(0xFF4CAF50),
    primaryContainer = Color(0xFF388E3C),
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
val coloresRojoClaro = lightColorScheme(
    primary = Color(0xFFF44336),         // Rojo
    primaryContainer = Color(0xFFD32F2F),
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

val coloresRojoOscuro = darkColorScheme(
    primary = Color(0xFFF44336),
    primaryContainer = Color(0xFFD32F2F),
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
val coloresMoradoClaro = lightColorScheme(
    primary = Color(0xFF9C27B0),         // Morado
    primaryContainer = Color(0xFF7B1FA2),
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

val coloresMoradoOscuro = darkColorScheme(
    primary = Color(0xFF9C27B0),
    primaryContainer = Color(0xFF7B1FA2),
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
val coloresGrisClaro = lightColorScheme(
    primary = Color(0xFF9E9E9E),         // Gris
    primaryContainer = Color(0xFF616161),
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

val coloresGrisOscuro = darkColorScheme(
    primary = Color(0xFF9E9E9E),
    primaryContainer = Color(0xFF616161),
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
val coloresNaranjaClaro = lightColorScheme(
    primary = Color(0xFFFF9800),         // Naranja
    primaryContainer = Color(0xFFF57C00),
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

val coloresNaranjaOscuro = darkColorScheme(
    primary = Color(0xFFFF9800),
    primaryContainer = Color(0xFFF57C00),
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