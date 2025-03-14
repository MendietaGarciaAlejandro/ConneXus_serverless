package org.connexuss.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.connexuss.project.interfaces.colores.TemaConfig
import org.connexuss.project.interfaces.colores.TemaConfigSaver
import org.connexuss.project.interfaces.colores.getColorsForTheme
import org.connexuss.project.interfaces.fuente.AppTheme
import org.connexuss.project.interfaces.fuente.ProveedorDeFuente
import org.connexuss.project.interfaces.idiomas.ProveedorDeIdioma
import org.connexuss.project.navegacion.Navegacion

@Composable
fun App() {
    // Estado para la configuración del tema
    var temaConfig by rememberSaveable(stateSaver = TemaConfigSaver) { mutableStateOf(TemaConfig()) }

    // Envuelve la app en el proveedor de idioma y fuente, según tu implementación
    ProveedorDeIdioma {
        ProveedorDeFuente {
            AppTheme {
                // Calcula la paleta de colores según el estado actual
                MaterialTheme(colors = getColorsForTheme(temaConfig.temaClaro, temaConfig.colorTemaKey)) {
                    Navegacion(
                        temaConfig = temaConfig,
                        onToggleTheme = {
                            temaConfig = temaConfig.copy(
                                temaClaro = !temaConfig.temaClaro
                            )
                        },
                        onColorChange = { nuevoKey ->
                            temaConfig = temaConfig.copy(colorTemaKey = nuevoKey)
                        }
                    )
                }
            }
        }
    }
}