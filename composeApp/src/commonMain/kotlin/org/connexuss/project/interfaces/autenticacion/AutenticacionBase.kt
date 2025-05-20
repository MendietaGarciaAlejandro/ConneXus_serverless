package org.connexuss.project.interfaces.autenticacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.getRestoredSessionFlow
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.UtilidadesUsuario
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable que muestra la pantalla de carga (Splash Screen).
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun SplashScreen(navController: NavHostController, settingsState: SettingsState) {
    // Efecto para esperar 2 segundos y navegar a "home"
    // Efecto que corre **una sola vez** en el lanzamiento del Composable
    LaunchedEffect(Unit) {
        delay(2000)
        // 1) Leemos tokens + usuario de forma atómica
        val restored = settingsState
            .getRestoredSessionFlow()        // Flow<Pair<UserSession,Usuario>?>
            .firstOrNull()                   // primer valor emitido

        if (restored != null) {
            // 2) Si existe, desestructura
            val (session, usuario) = restored

            // 3) Importar al cliente Supabase
            Supabase.client.auth.importSession(session)

            // 4) Reasignar globals para toda la app
            sesionActualUsuario = session
            UsuarioPrincipal = usuario

            // 5) Navegar a contactos, pop splash
            navController.navigate("contactos") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // 6) No había sesión: ir a login
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(Res.drawable.connexus),
                        contentDescription = "Ícono de la aplicación",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// Si el email NO está en el sistema
/**
 * Composable base para pantallas de verificación de email.
 *
 * @param navController controlador de navegación.
 * @param titleKey clave para el título de la pantalla.
 * @param mensajeKey clave para el mensaje mostrado.
 * @param mostrarCampoCodigo indica si se debe mostrar el campo de código.
 * @param textoBotonPrincipalKey clave para el texto del botón principal.
 * @param rutaBotonPrincipal ruta de navegación del botón principal.
 * @param textoBotonSecundarioKey clave para el texto del botón secundario.
 * @param rutaBotonSecundario ruta de navegación del botón secundario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaEmailBase(
    navController: NavHostController,
    titleKey: String,
    mensajeKey: String,
    mostrarCampoCodigo: Boolean = false,
    textoBotonPrincipalKey: String,
    rutaBotonPrincipal: String,
    textoBotonSecundarioKey: String? = null,
    rutaBotonSecundario: String? = null
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir(titleKey),
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
                actions = {
                    IconButton(onClick = { /* acción engranaje */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = traducir("ajustes")
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = "Ícono de la aplicación",
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Text(
                        text = traducir(mensajeKey),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    if (mostrarCampoCodigo) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text(traducir("codigo_verificacion")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { navController.navigate(rutaBotonPrincipal) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = traducir(textoBotonPrincipalKey),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        if (textoBotonSecundarioKey != null && rutaBotonSecundario != null) {
                            Button(
                                onClick = { navController.navigate(rutaBotonSecundario) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = traducir(textoBotonSecundarioKey),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Pantalla cuando el email NO está en el sistema
/**
 * Composable que muestra la pantalla para email no registrado.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun PantallaEmailNoEnElSistema(navController: NavHostController) {
    PantallaEmailBase(
        navController = navController,
        titleKey = "email_no_existe_titulo",
        mensajeKey = "email_no_existe_mensaje",
        textoBotonPrincipalKey = "ir_a_registro",
        rutaBotonPrincipal = "registro"
    )
}

// Pantalla cuando el email está en el sistema
/**
 * Composable que muestra la pantalla para email registrado.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun PantallaEmailEnElSistema(navController: NavHostController) {
    PantallaEmailBase(
        navController = navController,
        titleKey = "email_en_sistema_titulo",
        mensajeKey = "email_en_sistema_mensaje",
        mostrarCampoCodigo = true,
        textoBotonPrincipalKey = "restablecer_contrasena",
        rutaBotonPrincipal = "restableceContrasenna",
        textoBotonSecundarioKey = "cancelar",
        rutaBotonSecundario = "login"
    )
}

//metodo que comprueba correo
fun esEmailValido(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    return regex.matches(email)
}
