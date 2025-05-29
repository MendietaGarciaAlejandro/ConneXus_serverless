package org.connexuss.project.interfaces.autenticacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.notificaciones.scope
import org.connexuss.project.misc.Supabase
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Pantalla para solicitar el restablecimiento de contraseña mediante correo electrónico.
 *
 * @param navController Controlador de navegación para navegar entre pantallas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaRestablecer(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Realiza la traducción fuera del bloque onClick
    val errorCorreoVacio = traducir("error_correo_vacio")

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
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    OutlinedTextField(
                        singleLine = true,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                scope.launch {
                                    if (email.isBlank()) {
                                        errorMessage = "Introduce tu correo"
                                        return@launch
                                    }

                                    try {
                                        Supabase.client.auth.resetPasswordForEmail(email = email)
                                        mensaje = "📧 Se ha enviado un correo para restablecer tu contraseña. Revisa tu bandeja de entrada."
                                        errorMessage = ""
                                    } catch (e: Exception) {
                                        errorMessage = "❌ Error al enviar el correo: ${e.message}"
                                        mensaje = ""
                                    }
                                }
                            }
                            ,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("enviar_correo"))
                        }
                        FilledTonalButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("cancelar"))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { navController.navigate("emailEnSistema") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("degug_restablecer_ok"))
                        }
                        FilledTonalButton(
                            onClick = { navController.navigate("emailNoEnSistema") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("degug_restablecer_fail"))
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
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
fun PantallaRestablecimientoContrasenna(navController: NavHostController) {
    var contrasenna by remember { mutableStateOf("") }
    var confirmarContrasenna by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Realiza la traducción fuera del bloque onClick
    val errorContrasenas = traducir("error_contrasenas")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DefaultTopBar(
                title = traducir("restablecer_contrasena"),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = true,
                irParaAtras = true
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
                    modifier = modifier
                        .padding(padding)
                        .padding(16.dp),
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                errorMessage =
                                    if (contrasenna == confirmarContrasenna && contrasenna.isNotBlank()) {
                                        ""
                                    } else {
                                        errorContrasenas
                                    }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("restablecer"))
                        }
                        FilledTonalButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir("cancelar"))
                        }
                    }
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
