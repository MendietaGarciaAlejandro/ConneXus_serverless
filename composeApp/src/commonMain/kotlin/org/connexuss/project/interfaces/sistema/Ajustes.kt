package org.connexuss.project.interfaces.sistema

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.perfiles.UsuCard
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.clearSession
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable que muestra la pantalla de ajustes.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun muestraAjustes(
    navController: NavHostController = rememberNavController(),
    settingsState: SettingsState
) {
    val user = UsuarioPrincipal
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes"),
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false
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
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (user != null) {
                        // Se elimina el ElevatedCard exterior para evitar el solapamiento
                        UsuCard(
                            usuario = user,
                            onClick = {
                                navController.navigate("mostrarPerfilPrincipal")
                            },
                        )
                    }

                    // Opciones principales
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { navController.navigate("cambiarTema") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_modo_oscuro_tema"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("cambiaFuente") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_fuente"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("idiomas") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_idioma"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    // Opciones de gestión
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { /* Eliminar Chats */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text(
                                    text = traducir("eliminar_chats"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("control_de_cuentas"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("ajustesAyuda") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("ayuda"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    // Botón de cerrar sesión
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    Supabase.client.auth.signOut()
                                    settingsState.clearSession()
                                    sesionActualUsuario = null
                                    UsuarioPrincipal = null
                                    println("🔒 Sesión local y remota cerrada")
                                } catch (e: Exception) {
                                    println("❌ Error cerrando sesión: ${e.message}")
                                }
                            }
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = traducir("cerrar_sesion"),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
