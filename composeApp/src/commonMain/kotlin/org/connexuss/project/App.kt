package org.connexuss.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.connexuss.project.interfaces.TemaConfig
import org.connexuss.project.interfaces.TemaConfigSaver
import org.connexuss.project.interfaces.getColorsForTheme
import org.connexuss.project.interfaces.AppTheme
import org.connexuss.project.interfaces.ProveedorDeFuente
import org.connexuss.project.interfaces.ProveedorDeIdioma
import org.connexuss.project.navegacion.Navegacion
import org.connexuss.project.usuario.Usuario

// Declara la variable con setter privado si no quieres que se modifique desde fuera
var usuariosGrupoGeneral: List<Usuario> = emptyList()
    private set

// Para actualizar la lista, simplemente asigna a la propiedad:
fun actualizarUsuariosGrupoGeneral(nuevaLista: List<Usuario>) {
    usuariosGrupoGeneral = nuevaLista
}


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
                        },
                        listaUsuariosGrupo = usuariosGrupoGeneral,
                    )
                }
            }
        }
    }
}