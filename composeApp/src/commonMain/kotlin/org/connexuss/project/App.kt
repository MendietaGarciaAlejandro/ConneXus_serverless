package org.connexuss.project

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.connexuss.project.interfaces.fuente.AppTheme
import org.connexuss.project.interfaces.fuente.ProveedorDeFuente
import org.connexuss.project.interfaces.idiomas.ProveedorDeIdioma
import org.connexuss.project.navegacion.Navegacion

data class TemaConfig(
    val temaClaro: Boolean = true,
    val colores: Colors = coloresClaros
)

val TemaConfigSaver: Saver<TemaConfig, *> = listSaver(
    save = { listOf(it.temaClaro) },  // Guarda solo el booleano
    restore = { TemaConfig(it[0] as Boolean, if (it[0] as Boolean) coloresClaros else coloresOscuros) }
)

@Composable
fun App() {
    // Estado para la configuración del tema
    var temaConfig by rememberSaveable(stateSaver = TemaConfigSaver) { mutableStateOf(TemaConfig()) }

    // Envuelve la app en el proveedor de idioma para que el idioma se comparta globalmente
    ProveedorDeIdioma {
        ProveedorDeFuente {
            AppTheme {
                MaterialTheme(colors = temaConfig.colores) {
                    Navegacion(
                        temaConfig = temaConfig,
                        onToggleTheme = {
                            temaConfig = temaConfig.copy(
                                temaClaro = !temaConfig.temaClaro,
                                colores = if (temaConfig.temaClaro) coloresOscuros else coloresClaros
                            )
                        },
                        onColorChange = { nuevoColor ->
                            temaConfig = temaConfig.copy(colores = nuevoColor)
                        }
                    )
                }
            }
        }
    }
}