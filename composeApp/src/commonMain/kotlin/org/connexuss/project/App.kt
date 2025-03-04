package org.connexuss.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import kotlinx.datetime.Clock
import org.connexuss.project.comunicacion.ChatMessage
import org.connexuss.project.comunicacion.ChatRoom
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    // Descomenta y navega entre pantallas para ver la interfaz
    //muestraUsuarios()
    //pantallaRegistro()
    //pantallaLogin()
    //restableceContrasenna()
    muestraHomePage()
    //muestraChatRoom()
    //muestraContactos()
    //muestraAjustes()
    //muestraNuevoChat()
    //muestraChats()
    //muestraForo()
    //muestraTemaForo()

}

// --- Muestra Usuarios ---
@Composable
fun muestraUsuarios() {
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
}

// --- Pantalla Registro ---
@Composable
@Preview
fun pantallaRegistro() {
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
}

// --- Pantalla Login ---
@Composable
@Preview
fun pantallaLogin() {
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
                        // Implementar lógica de inicio de sesión
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colors.error, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// --- Restablecer Contraseña ---
@Composable
@Preview
fun restableceContrasenna() {
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
}

// --- Home Page ---
@Composable
@Preview
fun muestraHomePage() {
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
                Button(onClick = { /* Nuevo Chat */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Nuevo Chat")
                }
                Button(onClick = { /* Ver Chats */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Chats")
                }
                Button(onClick = { /* Ver Contactos */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Contactos")
                }
                Button(onClick = { /* Ajustes */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ajustes")
                }
            }
        }
    }
}

// --- Chat Room ---
@Composable
@Preview
fun muestraChatRoom() {
    val mensajes = remember { mutableStateListOf<ChatMessage>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Chat Room") },
                    navigationIcon = {
                        IconButton(onClick = { /* Navegar atrás */ }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(mensajes) { mensaje ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Text(
                                text = mensaje.content,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoMensaje,
                        onValueChange = { nuevoMensaje = it },
                        label = { Text("Nuevo Mensaje") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nuevoMensaje.isNotEmpty()) {
                            mensajes.add(
                                ChatMessage(
                                    id = Clock.System.now().toEpochMilliseconds().toString(),
                                    senderId = "user", // Aquí usar el ID del usuario autenticado
                                    content = nuevoMensaje
                                )
                            )
                            nuevoMensaje = ""
                        }
                    }) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

// --- Contactos ---
@Composable
@Preview
fun muestraContactos() {
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
}

// --- Ajustes ---
@Composable
@Preview
fun muestraAjustes() {
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
}

// --- Nuevo Chat ---
@Composable
@Preview
fun muestraNuevoChat() {
    var nombreChat by remember { mutableStateOf("") }
    val participantes = remember { mutableStateListOf<String>() }
    var nuevoParticipante by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Nuevo Chat") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nombreChat,
                    onValueChange = { nombreChat = it },
                    label = { Text("Nombre del Chat") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nuevoParticipante,
                        onValueChange = { nuevoParticipante = it },
                        label = { Text("Agregar Participante") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nuevoParticipante.isNotEmpty()) {
                            participantes.add(nuevoParticipante)
                            nuevoParticipante = ""
                        }
                    }) {
                        Text("Agregar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Participantes:", style = MaterialTheme.typography.subtitle1)
                LazyColumn {
                    items(participantes) { participante ->
                        Text(participante, modifier = Modifier.padding(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Lógica para crear el chat con 'nombreChat' y 'participantes' */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear Chat")
                }
            }
        }
    }
}

// --- Chats ---
@Composable
@Preview
fun muestraChats() {
    val chats = remember { mutableStateListOf<ChatRoom>() }
    var nuevoChat by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Chats") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nuevoChat,
                    onValueChange = { nuevoChat = it },
                    label = { Text("Nuevo Chat") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nuevoChat.isNotEmpty()) {
                            chats.add(ChatRoom(id = nuevoChat, participants = listOf("user1", "user2")))
                            nuevoChat = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar Chat")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(chats) { chat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Chat: ${chat.id}", style = MaterialTheme.typography.subtitle1)
                                Text("Participantes: ${chat.participants.joinToString(", ")}", style = MaterialTheme.typography.body2)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Foro ---
@Composable
@Preview
fun muestraForo() {
    val temas = remember { mutableStateListOf<String>() }
    var nuevoTema by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Foro") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nuevoTema,
                    onValueChange = { nuevoTema = it },
                    label = { Text("Nuevo Tema") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nuevoTema.isNotEmpty()) {
                            temas.add(nuevoTema)
                            nuevoTema = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar Tema")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(temas) { tema ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Text(
                                tema,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Tema del Foro ---
@Composable
@Preview
fun muestraTemaForo() {
    val mensajes = remember { mutableStateListOf<String>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tema del Foro") },
                    navigationIcon = {
                        IconButton(onClick = { /* Navegar atrás */ }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(mensajes) { mensaje ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Text(
                                text = mensaje,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoMensaje,
                        onValueChange = { nuevoMensaje = it },
                        label = { Text("Nuevo Mensaje") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nuevoMensaje.isNotEmpty()) {
                            mensajes.add(nuevoMensaje)
                            nuevoMensaje = ""
                        }
                    }) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}
