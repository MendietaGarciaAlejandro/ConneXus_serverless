package org.connexuss.project.interfaces.autenticacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.saveSession
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Pantalla de inicio de sesión para la aplicación.
 *
 * @param navController Controlador de navegación para navegar entre pantallas
 * @param settingsState Estado de configuración para guardar la sesión
 */
@Composable
@Preview
fun PantallaLogin(
    navController: NavHostController,
    settingsState: SettingsState
) {
    var emailInterno by rememberSaveable { mutableStateOf("") }
    var passwordInterno by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDebug by remember { mutableStateOf(false) }
    var logoTapCount by remember { mutableStateOf(0) }
    val tapsToReveal = 5
    val scope = rememberCoroutineScope()
    val repoSupabase = remember { SupabaseUsuariosRepositorio() }

    // Mensajes de error
    val porFavorCompleta = traducir("completa_todos_campos")
    val errorEmailNingunUsuario = traducir("email_no_encontrado")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(Res.drawable.connexus),
                        contentDescription = traducir("icono_app"),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                logoTapCount++
                                if (logoTapCount >= tapsToReveal) {
                                    showDebug = true
                                }
                            }
                    )
                }

                OutlinedTextField(
                    value = emailInterno,
                    onValueChange = { emailInterno = it },
                    label = { Text(traducir("email")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                OutlinedTextField(
                    value = passwordInterno,
                    onValueChange = { passwordInterno = it },
                    label = { Text(traducir("contrasena")) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = traducir("recuerdame"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilledTonalButton(
                        onClick = { navController.navigate("restablecer") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            traducir("olvidaste_contrasena"),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = { navController.navigate("registro") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            traducir("registrarse"),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                ElevatedButton(
                    onClick = {
                        scope.launch {
                            if (emailInterno.isBlank() || passwordInterno.isBlank()) {
                                errorMessage = porFavorCompleta
                                return@launch
                            }

                            try {
                                val usuario =
                                    repoSupabase.getUsuarioPorEmail(emailInterno.trim())
                                        .firstOrNull()

                                if (usuario == null) {
                                    errorMessage = errorEmailNingunUsuario
                                } else {
                                    UsuarioPrincipal = usuario
                                    println("Usuario autenticado: $UsuarioPrincipal")

                                    Supabase.client.auth.signInWith(Email) {
                                        email = emailInterno.trim()
                                        password = passwordInterno.trim()
                                    }

                                    sesionActualUsuario =
                                        Supabase.client.auth.currentSessionOrNull()
                                    errorMessage = ""

                                    if (rememberMe && sesionActualUsuario != null) {
                                        val userJson = Json.encodeToString(
                                            Usuario.serializer(),
                                            usuario
                                        )
                                        settingsState.saveSession(
                                            sesionActualUsuario!!,
                                            UsuarioPrincipal!!
                                        )
                                    }

                                    navController.navigate("contactos") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        traducir("acceder"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (showDebug) {
                    FilledTonalButton(
                        onClick = { navController.navigate("zonaPruebas") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Debug: Ir a la zona de pruebas",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
