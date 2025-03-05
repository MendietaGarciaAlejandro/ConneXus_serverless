package org.connexuss.project.interfaces.sistema

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.jetbrains.compose.ui.tooling.preview.Preview

// --- Muestra Usuarios ---
@Composable
fun muestraUsuarios(navController: NavHostController) {
    val almacenamientoUsuario = remember { AlmacenamientoUsuario() }
    val usuarios = remember { mutableStateListOf<Usuario>() }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val user1 = UtilidadesUsuario().instanciaUsuario("Juan Perez", 25, "paco@jerte.org", "pakito58", true)
            val user2 = UtilidadesUsuario().instanciaUsuario("Maria Lopez", 30, "marii@si.se", "marii", true)
            val user3 = UtilidadesUsuario().instanciaUsuario("Pedro Sanchez", 40, "roba@espannoles.es", "roba", true)
            almacenamientoUsuario.agregarUsuario(user1)
            almacenamientoUsuario.agregarUsuario(user2)
            almacenamientoUsuario.agregarUsuario(user3)
            usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Usuarios") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text("Mostrar Usuarios")
                }
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(visible = showContent) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(usuarios) { usuario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Nombre: ${usuario.getNombreCompleto()}", style = MaterialTheme.typography.subtitle1)
                                    Text("Alias: ${usuario.getAlias()}", style = MaterialTheme.typography.body1)
                                    Text("Activo: ${usuario.getActivo()}", style = MaterialTheme.typography.body2)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Puedes agregar aquí más detalles o acciones
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //navController.navigate("usuarios")
}

// --- Pantalla Registro ---
@Composable
@Preview
fun pantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Registro") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        errorMessage = if (password == confirmPassword && password.isNotBlank()) {
                            // Aquí iría la lógica real de registro
                            ""
                        } else {
                            "Las contraseñas no coinciden o están vacías"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar")
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colors.error, textAlign = TextAlign.Center)
                }
            }
        }
    }
    //navController.navigate("registro")
}

// --- Pantalla Login ---
@Composable
@Preview
fun pantallaLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Iniciar Sesión") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Lógica de autenticación...
                        // Si es correcto -> navController.navigate("home") u otra ruta
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMessage,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


// --- Restablecer Contraseña ---
@Composable
@Preview
fun restableceContrasenna(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Restablecer Contraseña") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Lógica para enviar el correo de restablecimiento
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Correo")
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colors.error, textAlign = TextAlign.Center)
                }
            }
        }
    }
    //navController.navigate("restablecer")
}

// --- Home Page ---
@Composable
@Preview
fun muestraHomePage(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("ConneXus") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        // Aquí sí se hace la navegación cuando el usuario pulsa el botón
                        navController.navigate("login")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a Login")
                }

                Button(
                    onClick = {
                        navController.navigate("registro")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a Registro")
                }

                Button(
                    onClick = {
                        navController.navigate("contactos")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Contactos")
                }

                Button(
                    onClick = {
                        navController.navigate("ajustes")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ajustes")
                }
            }
        }
    }
}


// --- Contactos ---
@Composable
@Preview
fun muestraContactos(navController: NavHostController) {
    val almacenamientoUsuario = remember { AlmacenamientoUsuario() }
    val usuarios = remember { mutableStateListOf<Usuario>() }

    LaunchedEffect(Unit) {
        try {
            val user1 = UtilidadesUsuario().instanciaUsuario("Juan Perez", 25, "paco@jerte.org", "pakito58", true)
            val user2 = UtilidadesUsuario().instanciaUsuario("Maria Lopez", 30, "marii@si.se", "marii", true)
            val user3 = UtilidadesUsuario().instanciaUsuario("Pedro Sanchez", 40, "roba@espannoles.es", "roba", true)
            almacenamientoUsuario.agregarUsuario(user1)
            almacenamientoUsuario.agregarUsuario(user2)
            almacenamientoUsuario.agregarUsuario(user3)
            usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Contactos") }) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nombre: ${usuario.getNombreCompleto()}", style = MaterialTheme.typography.subtitle1)
                            Text("Alias: ${usuario.getAlias()}", style = MaterialTheme.typography.body1)
                        }
                    }
                }
            }
        }
    }
    //navController.navigate("contactos")
}

// --- Ajustes ---
@Composable
@Preview
fun muestraAjustes(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Ajustes") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { /* Cambiar Tema */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cambiar Tema")
                }
                Button(onClick = { /* Cambiar Idioma */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cambiar Idioma")
                }
                Button(onClick = { /* Cerrar Sesión */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
    //navController.navigate("ajustes")
}