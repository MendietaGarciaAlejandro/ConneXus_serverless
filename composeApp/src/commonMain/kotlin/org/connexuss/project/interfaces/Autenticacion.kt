package org.connexuss.project.interfaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.saveSession
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaRestablecer(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir("restablecer_contrasena"),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = traducir("volver")
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier.padding(24.dp),
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
                            contentDescription = "Logo",
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(traducir("email")) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    FilledTonalButton(
                        onClick = {
                            scope.launch {
                                if (email.isBlank()) {
                                    error = "Introduce tu correo"
                                    return@launch
                                }

                                try {
                                    Supabase.client.auth.resetPasswordForEmail(email)
                                    mensaje =
                                        "📧 Se ha enviado un correo para restablecer tu contraseña. Ábrelo desde tu navegador y sigue los pasos."
                                    error = ""
                                } catch (e: Exception) {
                                    error = "❌ Error al enviar el correo: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Email,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                traducir("enviar_correo_restablecimiento"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    if (mensaje.isNotEmpty()) {
                        Text(
                            mensaje,
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    if (error.isNotEmpty()) {
                        Text(
                            error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            "Una vez restablezcas tu contraseña desde el navegador, vuelve a esta app y entra con tu nueva clave.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Button(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            traducir("volver_login"),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable que muestra la pantalla para restablecer la contraseña ingresando una nueva.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun muestraRestablecimientoContasenna(navController: NavHostController) {
    var contrasenna by remember { mutableStateOf("") }
    var confirmarContrasenna by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val repo = remember { SupabaseUsuariosRepositorio() }

    val errorContrasenas = traducir("error_contrasenas")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DefaultTopBar(
                title = traducir("restablecer_contrasena"),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = false,
                irParaAtras = false
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier.padding(24.dp),
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
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = contrasenna,
                        onValueChange = { contrasenna = it },
                        label = { Text(traducir("contrasena")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = confirmarContrasenna,
                        onValueChange = { confirmarContrasenna = it },
                        label = { Text(traducir("confirmar_contrasena")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    FilledTonalButton(
                        onClick = {
                            if (contrasenna != confirmarContrasenna || contrasenna.isBlank()) {
                                errorMessage = errorContrasenas
                                return@FilledTonalButton
                            }

                            scope.launch {
                                try {
                                    val user = Supabase.client.auth.currentUserOrNull()
                                    if (user == null) {
                                        errorMessage = "⚠️ No hay sesión activa para actualizar."
                                        return@launch
                                    }

                                    // 1. Actualizar en Auth
                                    Supabase.client.auth.updateUser {
                                        password = contrasenna
                                    }

                                    // 2. Actualizar también en la tabla usuario
                                    repo.updateCampo(
                                        tabla = "usuario",
                                        campo = "contrasennia",
                                        valor = contrasenna,
                                        idCampo = "idunico",
                                        idValor = user.id
                                    )

                                    mensaje = "✅ Contraseña restablecida con éxito"
                                    errorMessage = ""
                                    navController.navigate("login") {
                                        popUpTo("restablecerNueva") { inclusive = true }
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "❌ Error: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                traducir("restablecer"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    if (mensaje.isNotEmpty()) {
                        Text(
                            mensaje,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}