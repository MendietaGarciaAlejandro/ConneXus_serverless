package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.launch
import org.connexuss.project.persistencia.FontKeys
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.getFontKeyFlow
import org.connexuss.project.persistencia.getTemaConfigFlow
import org.connexuss.project.persistencia.setFontKey

/**
 * CompositionLocal that holds the mutable state of the current FontFamily.
 */
val LocalFontState = staticCompositionLocalOf<FontFamily> {
    error("No se ha proporcionado FontFamily")
}

data class FontOption(
    val code: String,          // lo que guarda SettingsState (FontKeys.DEFAULT, “serif”, …)
    val labelKey: String,      // la clave que pasas a traducir(), p.ej. “default_font”, “serif_font”, …
    val family: FontFamily     // la FontFamily real que usarás en el tema
)

// en PantallaCambiarFuente.kt o donde construyas las opciones
val fontOptions = listOf(
    FontOption(FontKeys.DEFAULT,    "default_font",    FontFamily.Default),
    FontOption(FontKeys.SERIF,      "serif_font",      FontFamily.Serif),
    FontOption(FontKeys.MONOSPACE,  "monospace_font",  FontFamily.Monospace),
    FontOption(FontKeys.CURSIVE,    "cursive_font",    FontFamily.Cursive),
    FontOption(FontKeys.SANS_SERIF, "sans_serif_font", FontFamily.SansSerif)
)

/**
 * Provides the global font state to its content.
 *
 * @param content Composable lambda that will use the font state.
 */
@OptIn(ExperimentalSettingsApi::class)
@Composable
fun ProveedorDeFuente(
    settingsState: SettingsState,
    content: @Composable () -> Unit
) {
    val fontKey by settingsState.getFontKeyFlow().collectAsState(initial = FontKeys.DEFAULT)

    val fontFamily = when (fontKey) {
        FontKeys.SERIF      -> FontFamily.Serif
        FontKeys.MONOSPACE  -> FontFamily.Monospace
        FontKeys.CURSIVE    -> FontFamily.Cursive
        FontKeys.SANS_SERIF -> FontFamily.SansSerif
        else                -> FontFamily.Default
    }

    // Exponemos directamente FontFamily (no MutableState intermedio)
    CompositionLocalProvider(LocalFontState provides fontFamily) {
        content()
    }
}

/**
 * Applies MaterialTheme using the global font state.
 *
 * @param content Composable lambda representing the themed UI.
 */
@Composable
fun AppTheme(settingsState: SettingsState, content: @Composable () -> Unit) {

    // 2) Crea tu esquema de Material 3 (colores, shapes…)
    val temaConfig by settingsState.getTemaConfigFlow().collectAsState(initial = TemaConfig())
    val colorScheme = getColorsForTheme(temaConfig.temaClaro, temaConfig.colorTemaKey)

    val base        = Typography()
    val ff          = LocalFontState.current
    val typography  = Typography(
        displayLarge   = base.displayLarge.copy(fontFamily = ff),
        displayMedium  = base.displayMedium.copy(fontFamily = ff),
        displaySmall   = base.displaySmall.copy(fontFamily = ff),
        headlineLarge  = base.headlineLarge.copy(fontFamily = ff),
        headlineMedium = base.headlineMedium.copy(fontFamily = ff),
        headlineSmall  = base.headlineSmall.copy(fontFamily = ff),
        titleLarge     = base.titleLarge.copy(fontFamily = ff),
        titleMedium    = base.titleMedium.copy(fontFamily = ff),
        titleSmall     = base.titleSmall.copy(fontFamily = ff),
        bodyLarge      = base.bodyLarge.copy(fontFamily = ff),
        bodyMedium     = base.bodyMedium.copy(fontFamily = ff),
        bodySmall      = base.bodySmall.copy(fontFamily = ff),
        labelLarge     = base.labelLarge.copy(fontFamily = ff),
        labelMedium    = base.labelMedium.copy(fontFamily = ff),
        labelSmall     = base.labelSmall.copy(fontFamily = ff)
    )
    val shapes      = Shapes(
        small = MaterialTheme.shapes.small,
        medium = MaterialTheme.shapes.medium,
        large = MaterialTheme.shapes.large,
        extraSmall = MaterialTheme.shapes.extraSmall,
        extraLarge = MaterialTheme.shapes.extraLarge
    )

    // 3) Aplica MaterialTheme y, dentro, overridea el TextStyle global
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes
    ) {
//        CompositionLocalProvider(
//            // TO-DO: si quieres también un LocalTextStyle específico para body/heading,
//            // puedes cambiar el estilo base aquí (size, weight…)
//            LocalTextStyle provides TextStyle(fontFamily = fontFamily)
//        ) {
//            content()
//        }
        content()
    }
}

/**
 * Displays the screen to change the font.
 *
 * @param navController Navigation controller for handling navigation events.
 */
@Composable
fun PantallaCambiarFuente(
    navController: NavHostController,
    settingsState: SettingsState
) {
    val scope = rememberCoroutineScope()
    val fontState = LocalFontState.current
    val currentKey by settingsState.getFontKeyFlow().collectAsState(initial = FontKeys.DEFAULT)

    // Opciones: clave y familia (para mostrar la preview)
//    val fontOptions = listOf(
//        FontKeys.DEFAULT    to FontFamily.Default,
//        FontKeys.SERIF      to FontFamily.Serif,
//        FontKeys.MONOSPACE  to FontFamily.Monospace,
//        FontKeys.CURSIVE    to FontFamily.Cursive,
//        FontKeys.SANS_SERIF to FontFamily.SansSerif
//    )

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes_fuente"),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = false,
                irParaAtras = true
            )
        }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(traducir("cambiar_fuente"), style = MaterialTheme.typography.headlineSmall)
                fontOptions.forEach { option ->
                    val selected = option.code == currentKey
                    Button(
                        onClick = {
                            scope.launch { settingsState.setFontKey(option.code) }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = if (selected)
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        else
                            ButtonDefaults.buttonColors()
                    ) {
                        Text(traducir(option.labelKey))
                    }
                }
            }
        }
    }
}