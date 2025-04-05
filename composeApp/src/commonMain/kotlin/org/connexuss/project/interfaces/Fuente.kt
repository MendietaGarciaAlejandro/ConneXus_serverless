package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * CompositionLocal that holds the mutable state of the current FontFamily.
 */
val LocalFontState = staticCompositionLocalOf<MutableState<FontFamily>> {
    error("No se ha proporcionado un estado de fuente")
}

/**
 * Provides the global font state to its content.
 *
 * @param content Composable lambda that will use the font state.
 */
@Composable
fun ProveedorDeFuente(content: @Composable () -> Unit) {
    val fontState = remember { mutableStateOf<FontFamily>(FontFamily.Default) }
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
    // Usa el estado global de la fuente para definir la tipografía
    val fontFamily = LocalFontState.current.value
    MaterialTheme(
        typography = Typography(defaultFontFamily = fontFamily),
        content = content
    )
}

/**
 * Displays the screen to change the font.
 *
 * @param navController Navigation controller for handling navigation events.
 */
@Composable
fun PantallaCambiarFuente(navController: NavHostController) {
    // Lista de opciones de fuentes: cada par tiene una clave de traducción y la FontFamily correspondiente.
    val fontOptions = listOf(
        "default_font" to FontFamily.Default,
        "serif_font" to FontFamily.Serif,
        "monospace_font" to FontFamily.Monospace,
        "cursive_font" to FontFamily.Cursive,
        "sans_serif_font" to FontFamily.SansSerif
    )

    // Obtén el estado global de la fuente
    val fontState = LocalFontState.current

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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        traducir("fuente"),
                        style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(traducir("cambiar_fuente"), style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Lista de opciones para seleccionar la fuente
                    fontOptions.forEach { (fontKey, fontFamily) ->
                        Button(
                            onClick = { fontState.value = fontFamily },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir(fontKey))
                        }
                    }
                }
            }
        }
    }
}