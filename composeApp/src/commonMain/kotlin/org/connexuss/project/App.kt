package org.connexuss.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.tema.TemaConfig
import org.connexuss.project.interfaces.tema.AppTheme
import org.connexuss.project.interfaces.tema.AppThemeWrapper
import org.connexuss.project.interfaces.tema.ProveedorDeFuente
import org.connexuss.project.interfaces.comun.ProveedorDeIdioma
import org.connexuss.project.navegacion.Navegacion
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.flowSettings
import org.connexuss.project.persistencia.getTemaConfigFlow
import org.connexuss.project.persistencia.setTemaConfig
import org.connexuss.project.usuario.Usuario

// Declara la variable con setter privado si no quieres que se modifique desde fuera
var usuariosGrupoGeneral: List<Usuario> = emptyList()
    private set

/**
 * Actualiza la lista global de usuarios para el grupo general.
 *
 * @param nuevaLista nueva lista de usuarios.
 */
fun actualizarUsuariosGrupoGeneral(nuevaLista: List<Usuario>) {
    usuariosGrupoGeneral = nuevaLista
}


/**
 * Función principal de la aplicación Compose.
 * Envuelve la aplicación en proveedores de idioma y fuente, y configura el tema.
 */
@OptIn(ExperimentalSettingsApi::class)
@Composable
fun App() {

    val settingsState = remember { SettingsState(flowSettings) }
    //val navController  = rememberNavController()
    val scope = rememberCoroutineScope()

    // Restaurar sesión Supabase
//    LaunchedEffect(Unit) {
//        settingsState.getSessionFlow().firstOrNull()?.let {
//            Supabase.client.auth.importSession(it)
//        }
//    }

//    LaunchedEffect(Unit) {
//        settingsState.getSessionFlow()
//            .firstOrNull()
//            ?.let { session ->
//                // Importamos la sesión en Supabase
//                Supabase.client.auth.importSession(session)
//                // Navegamos directamente al listado de contactos
//                navController.navigate("contactos") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//    }

    // Contenedor raíz que protege de notch + barras de sistema
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Padding para status and navigation bars
            .systemBarsPadding()
            // Padding para display cutout (notch)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        // Estado para la configuración del tema
        //var temaConfig by rememberSaveable(stateSaver = TemaConfigSaver) { mutableStateOf(TemaConfig()) }

        // Envuelve la app en el proveedor de idioma y fuente, según tu implementación
        ProveedorDeIdioma(settingsState) {
            ProveedorDeFuente((settingsState)) {
                val temaConfig by settingsState.getTemaConfigFlow().collectAsState(initial = TemaConfig())

                AppTheme {
                    // Calcula la paleta de colores según el estado actual
                    AppThemeWrapper(settingsState) {
                        Navegacion(
                            temaConfig = temaConfig,
                            onToggleTheme = {
                                // Cambia entre tema claro y oscuro
                                scope.launch {
                                    settingsState.setTemaConfig(temaConfig.copy(temaClaro = !temaConfig.temaClaro))
                                }
                            },
                            onColorChange = { nuevoKey ->
                                // Cambia el color del tema
                                scope.launch {
                                    settingsState.setTemaConfig(temaConfig.copy(colorTemaKey = nuevoKey))
                                }
                            },
                            listaUsuariosGrupo = usuariosGrupoGeneral,
                            settingsState = settingsState
                        )
                    }
                }
            }
        }
    }
}