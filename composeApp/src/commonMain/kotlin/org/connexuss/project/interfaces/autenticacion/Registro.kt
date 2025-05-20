package org.connexuss.project.interfaces.autenticacion

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.connexuss.project.encriptacion.EncriptacionResumenUsuario
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.misc.Supabase
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.UtilidadesUsuario
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable que muestra la pantalla de registro de usuario.
 *
 * @param navController controlador de navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var emailInterno by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val repoSupabase = SupabaseUsuariosRepositorio()
    val errorContrasenas = traducir("error_contrasenas")
    val errorEmailYaRegistrado = traducir("error_email_ya_registrado")
    val usuario = Usuario()
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir("registro"),
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
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
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
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(traducir("nombre")) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

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
                        value = password,
                        onValueChange = { password = it },
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
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text(traducir("confirmar_contrasena")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                errorMessage =
                                    if (password == confirmPassword && password.isNotBlank()) {
                                        ""
                                    } else {
                                        errorContrasenas
                                    }

                                if (errorMessage.isEmpty()) {
                                    scope.launch {
                                        try {
                                            val emailTrimmed = emailInterno.trim()

                                            if (!esEmailValido(emailTrimmed)) {
                                                errorMessage = "Formato de correo inválido"
                                                return@launch
                                            }

                                            val authResult =
                                                Supabase.client.auth.signUpWith(Email) {
                                                    this.email = emailTrimmed
                                                    this.password = password
                                                }
                                            navController.navigate("registroVerificaCorreo/${emailTrimmed}/${nombre}/${password}")

                                        } catch (e: Exception) {
                                            errorMessage = "❌ Error al registrar: ${e.message}"
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    traducir("registrar"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                traducir("cancelar"),
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
}

/**
 * Composable que muestra la pantalla de verificación de correo.
 *
 * @param navController controlador de navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaVerificaCorreo(
    navController: NavHostController,
    email: String?,
    nombre: String?,
    password: String?
) {
    val scope = rememberCoroutineScope()
    val repo = remember { SupabaseUsuariosRepositorio() }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir("verificacion_correo"),
                        style = MaterialTheme.typography.headlineMedium
                    )
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
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = traducir("correo_enviado_a"),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = email ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = traducir("verifica_cuenta_continuar"),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = {
                            scope.launch {
                                try {
                                    if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                                        Supabase.client.auth.signInWith(Email) {
                                            this.email = email
                                            this.password = password
                                        }
                                    }

                                    val usuarioActual = Supabase.client.auth.currentUserOrNull()

                                    if (usuarioActual?.emailConfirmedAt != null) {
                                        val imagenAleatoria = UtilidadesUsuario().generarImagenPerfilAleatoria()
                                        // Suponiendo que ya tienes `password` en memoria:
                                        val hash = EncriptacionResumenUsuario.hashPassword(password ?: EncriptacionResumenUsuario.hashPassword("prueba123"))

                                        val nuevoUsuario = Usuario(
                                            idUnico = usuarioActual.id,
                                            correo = email ?: "",
                                            nombre = nombre ?: "",
                                            aliasPrivado = "Privado_$nombre",
                                            aliasPublico = UtilidadesUsuario().generarAliasPublico(),
                                            activo = true,
                                            descripcion = "Perfil creado automáticamente",
                                            contrasennia = hash,
                                            imagenPerfilId = imagenAleatoria.id
                                        ).apply {
                                            // Guarda la contraseña en texto plano SOLO en propiedad transitoria
                                            contrasenniaPlain = password
                                            imagenPerfil = imagenAleatoria.resource
                                        }

                                        // Inserta en Supabase
                                        repo.addUsuario(nuevoUsuario)

                                        // Ahora `nuevoUsuario.contrasenniaPlain` sigue disponible en memoria
                                        navController.navigate("login") {
                                            popUpTo("registroVerificaCorreo") { inclusive = true }
                                        }
                                    } else {
                                        mensaje = "❗ Tu correo aún no está verificado."
                                    }
                                } catch (e: Exception) {
                                    navController.navigate("login")
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
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = traducir("ya_verificado"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                try {
                                    if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                                        Supabase.client.auth.signUpWith(Email) {
                                            this.email = email
                                            this.password = password
                                        }
                                        mensaje = "📧 Correo reenviado correctamente."
                                    } else {
                                        mensaje = "⚠️ Falta información para reenviar el correo."
                                    }
                                } catch (e: Exception) {
                                    mensaje = "❌ Error al reenviar: ${e.message}"
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
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = traducir("reenviar_correo"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    if (mensaje.isNotEmpty()) {
                        Text(
                            text = mensaje,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (mensaje.startsWith("❌")) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
