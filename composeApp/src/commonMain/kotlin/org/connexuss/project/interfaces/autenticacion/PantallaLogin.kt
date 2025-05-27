package org.connexuss.project.interfaces.autenticacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.connexuss.project.encriptacion.EncriptacionResumenUsuario
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
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
    var recordarEmail by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var mensajeBienvenido by remember { mutableStateOf("") }
    var showDebug by remember { mutableStateOf(false) }
    var logoTapCount by remember { mutableStateOf(0) }
    val tapsToReveal = 5
    val scope = rememberCoroutineScope()
    val repoSupabase = remember { SupabaseUsuariosRepositorio() }

    // Mensajes de error
    val errorEmailNingunUsuario = traducir("error_email_ningun_usuario")
    val errorContrasenaIncorrecta = traducir("error_contrasena_incorrecta")
    val porFavorCompleta = traducir("por_favor_completa")

    val visibilidadOn = Res.drawable.visibilidadOn
    val visibilidadOff = Res.drawable.visibilidadOff
    var verContra: Boolean by remember { mutableStateOf(false) }

    // Cargar email guardado si existe
    LaunchedEffect(Unit) {
        val savedEmail = settingsState.getEmail()
        if (savedEmail.isNotEmpty()) {
            emailInterno = savedEmail
        }

        // Cargar estado de los checkboxes
        rememberMe = settingsState.getRememberMeState()
        recordarEmail = settingsState.getRememberEmailState()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = passwordInterno,
                        onValueChange = { passwordInterno = it },
                        label = { Text(traducir("contrasena")) },
                        // Cuando verContra == true mostramos texto plano, si no, puntos
                        visualTransformation = if (verContra) VisualTransformation.None else PasswordVisualTransformation(),
                        // Ajustamos también el tipo de teclado
                        keyboardOptions = if (verContra) {
                            KeyboardOptions(keyboardType = KeyboardType.Text)
                        } else {
                            KeyboardOptions(keyboardType = KeyboardType.Password)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Image(
                        painter = painterResource(if (verContra) visibilidadOn else visibilidadOff),
                        contentDescription = traducir("toggle_ver_contrasena"),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { verContra = !verContra }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = {
                            rememberMe = it
                            // Guardar el estado del checkbox cuando cambia
                            scope.launch {
                                settingsState.saveRememberMeState(it)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Mantener sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Checkbox(
                        checked = recordarEmail,
                        onCheckedChange = {
                            recordarEmail = it
                            // Guardar el estado del checkbox cuando cambia
                            scope.launch {
                                settingsState.saveRememberEmailState(it)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Recordar email",
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

                    FilledTonalButton(
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
                        //TODO: Borrar autenticación local y ponerla solo con Supabase
                        scope.launch {
                            if (emailInterno.isBlank() || passwordInterno.isBlank()) {
                                errorMessage = porFavorCompleta
                                return@launch
                            }

                            try {
                                // Verificamos primero si el usuario existe en nuestra base de datos
                                val usuario =
                                    repoSupabase.getUsuarioPorEmail(emailInterno.trim()).first()
                                        ?: run {
                                            errorMessage = errorEmailNingunUsuario; return@launch
                                        }

                                // Validamos la contraseña con el resumen local
                                if (!EncriptacionResumenUsuario.checkPassword(
                                        passwordInterno,
                                        usuario.contrasennia
                                    )
                                ) {
//                                    errorMessage = errorContrasenaIncorrecta
//                                    return@launch
                                }

                                // Almacenamos el usuario para su uso en la aplicación
                                UsuarioPrincipal = usuario

                                // Intentamos autenticar con Supabase
                                try {
                                    // Iniciar sesión en Supabase
                                    Supabase.client.auth.signInWith(
                                        provider = Email
                                    ) {
                                        email = usuario.correo
                                        password = passwordInterno
                                    }

                                    // Si llegamos aquí, la autenticación con Supabase fue exitosa
                                    // Actualizar la sesión actual
                                    sesionActualUsuario =
                                        Supabase.client.auth.currentSessionOrNull()

                                    // Solo si tenemos sesión válida procedemos
                                    if (sesionActualUsuario != null) {
                                        // Mostrar mensaje de bienvenida
                                        mensajeBienvenido =
                                            "Bienvenido ${UsuarioPrincipal!!.nombre}"
                                        
                                        //TODO: Hacer que los checkboxes sean excluyentes

                                        // Persistir si rememberMe está activo
                                        if (rememberMe) {
                                            settingsState.saveSession(
                                                sesionActualUsuario!!,
                                                UsuarioPrincipal!!
                                            )
                                        }

                                        // Guardar email si el usuario lo solicitó
                                        if (recordarEmail) {
                                            settingsState.saveEmail(emailInterno)
                                        }

                                        // Esperar para mostrar el mensaje antes de navegar
                                        delay(1000)

                                        // Navegar a contactos
                                        navController.navigate("contactos") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = "Error de autenticación en Supabase"
                                    }
                                } catch (e: Exception) {
                                    // Error de autenticación en Supabase
                                    errorMessage = "Error en autenticación: ${e.message}"
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

                if (mensajeBienvenido.isNotEmpty()) {
                    Text(
                        mensajeBienvenido,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
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
