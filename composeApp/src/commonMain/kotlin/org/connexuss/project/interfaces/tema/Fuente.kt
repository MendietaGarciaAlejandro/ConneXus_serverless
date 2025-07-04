package org.connexuss.project.interfaces.tema

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.persistencia.FontKeys
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.getFontKeyFlow
import org.connexuss.project.persistencia.setFontKey

/**
 * CompositionLocal that holds the mutable state of the current FontFamily.
 */
val LocalFontState = staticCompositionLocalOf<MutableState<FontFamily>> {
    error("No se ha proporcionado un estado de fuente")
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
    // 1) Leemos la clave guardada como String
    val fontKey by settingsState.getFontKeyFlow()
        .collectAsState(initial = FontKeys.DEFAULT)

    // 2) La mapeamos inmediatamente a FontFamily
    val fontFamily = remember(fontKey) {
        when (fontKey) {
            FontKeys.SERIF      -> FontFamily.Serif
            FontKeys.MONOSPACE  -> FontFamily.Monospace
            FontKeys.CURSIVE    -> FontFamily.Cursive
            FontKeys.SANS_SERIF -> FontFamily.SansSerif
            else                -> FontFamily.Default
        }
    }

    // 3) Exponemos un MutableState que Compose reobserve
    val fontState = remember { mutableStateOf(fontFamily as FontFamily) }

    // 4) Cada vez que cambie `fontFamily` actualizamos fontState
    LaunchedEffect(fontFamily) {
        fontState.value = fontFamily
    }

    CompositionLocalProvider(LocalFontState provides fontState) {
        content()
    }
}

/**
 * Applies MaterialTheme using the global font state.
 *
 * @param content Composable lambda representing the themed UI.
 */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val fontFamily = LocalFontState.current.value

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = with(MaterialTheme.typography) {
            Typography(
                bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
                bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
                bodySmall = bodySmall.copy(fontFamily = fontFamily),
                titleLarge = titleLarge.copy(fontFamily = fontFamily),
                titleMedium = titleMedium.copy(fontFamily = fontFamily),
                titleSmall = titleSmall.copy(fontFamily = fontFamily),
                headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
                headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
                headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
                labelSmall = labelSmall.copy(fontFamily = fontFamily),
                labelMedium = labelMedium.copy(fontFamily = fontFamily),
                labelLarge = labelLarge.copy(fontFamily = fontFamily)
            )
        },
        content = content
    )
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
                Text(traducir("cambiar_fuente"), style = MaterialTheme.typography.titleLarge)
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

