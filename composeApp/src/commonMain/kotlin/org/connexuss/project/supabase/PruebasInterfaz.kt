package org.connexuss.project.supabase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho

// Inicializa el cliente de Supabase con tus credenciales
val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)

@Composable
fun SupabasePruebasInterfaz(navHostController: NavHostController) {
    // Aquí se muestran botones para probar la interación con los objetos de la app,
    // y las tablas de Supabase (navega a la pantalla cada objeto pulsando el botón)
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Pruebas de Interfaz",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
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
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Pruebas de interfaz", style = MaterialTheme.typography.h6)
                        Button(
                            onClick = { navHostController.navigate("supabaseUsuariosCRUD") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Usuarios")
                        }
                        Button(
                            onClick = { /*navHostController.navigate("supabaseUsuariosBloqueados")*/ },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de UsuariosBloqueados")
                        }
                        Button(
                            onClick = { /*navHostController.navigate("supabaseUsuariosContactos")*/ },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de UsuariosContactos")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasConversaciones") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Conversaciones")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasMensajes") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Mensajes")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasConversacionesUsuarioCRUD") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de ConversacionesUsuario")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasTemasCRUD") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Temas")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasHilosCRUD") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Hilos")
                        }
                        Button(
                            onClick = { navHostController.navigate("supabasePruebasPostsCRUD") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("CRUD de Posts")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupabaseMensajesCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var mensajes by remember { mutableStateOf(emptyList<Mensaje>()) }
    // Campos para el formulario de inserción
    var senderId by remember { mutableStateOf("") }
    var receiverId by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // Función para recargar mensajes
    fun cargarMensajes() {
        scope.launch {
            repository.getAll<Mensaje>("mensajes").collect { mensajes = it }
        }
    }

    LaunchedEffect(Unit) { cargarMensajes() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Mensajes",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Mensajes registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(mensajes) { mensaje ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${mensaje.senderId} -> ${mensaje.receiverId}: ${mensaje.content}")
                                // Botón de eliminación (para pruebas)
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<Mensaje>("mensajes", "id", mensaje.id)
                                        cargarMensajes()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = senderId,
                        onValueChange = { senderId = it },
                        label = { Text("Sender ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = receiverId,
                        onValueChange = { receiverId = it },
                        label = { Text("Receiver ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                repository.addItem(
                                    "mensajes",
                                    Mensaje(
                                        senderId = senderId,
                                        receiverId = receiverId,
                                        content = content
                                    )
                                )
                                senderId = ""
                                receiverId = ""
                                content = ""
                                cargarMensajes()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Mensaje")
                    }
                }
            }
        }
    }
}

@Composable
fun SupabaseConversacionesCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var conversaciones by remember { mutableStateOf(emptyList<Conversacion>()) }
    var nombre by remember { mutableStateOf("") }
    // Para simplicidad, aquí asumimos que solo se ingresa un nombre para la conversación.

    fun cargarConversaciones() {
        scope.launch {
            repository.getAll<Conversacion>("conversaciones").collect { conversaciones = it }
        }
    }

    LaunchedEffect(Unit) { cargarConversaciones() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Conversaciones",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Conversaciones registradas", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(conversaciones) { conv ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Conversación: ${conv.nombre ?: "Sin nombre"}  \nParticipantes: ${conv.participants.joinToString()}")
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<Conversacion>(
                                            "conversaciones",
                                            "id",
                                            conv.id
                                        )
                                        cargarConversaciones()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Conversación") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                // Para insertar, se debe construir un objeto Conversacion.
                                // Aquí se inserta con participantes vacíos y sin mensajes.
                                repository.addItem(
                                    "conversaciones",
                                    Conversacion(
                                        id = generateId(),
                                        participants = emptyList(),
                                        nombre = nombre
                                    )
                                )
                                nombre = ""
                                cargarConversaciones()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Conversación")
                    }
                }
            }
        }
    }
}

@Composable
fun SupabaseConversacionesUsuarioCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var convUsuarioList by remember { mutableStateOf(emptyList<ConversacionesUsuario>()) }
    var idUser by remember { mutableStateOf("") }
    // Se asume que 'conversaciones' se inicializa vacío o con algún valor predefinido.

    fun cargarConversacionesUsuario() {
        scope.launch {
            repository.getAll<ConversacionesUsuario>("conversacionesusuarios").collect { convUsuarioList = it }
        }
    }

    LaunchedEffect(Unit) { cargarConversacionesUsuario() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de ConversacionesUsuario",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("ConversacionesUsuario registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(convUsuarioList) { convUser ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Usuario: ${convUser.idUser}  - Conversaciones: ${convUser.conversaciones.size}")
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<ConversacionesUsuario>(
                                            "conversacionesusuarios",
                                            "id",
                                            convUser.id
                                        )
                                        cargarConversacionesUsuario()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = idUser,
                        onValueChange = { idUser = it },
                        label = { Text("ID del Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                repository.addItem(
                                    "conversacionesusuarios",
                                    ConversacionesUsuario(
                                        id = generateId(),
                                        idUser = idUser,
                                        conversaciones = emptyList()
                                    )
                                )
                                idUser = ""
                                cargarConversacionesUsuario()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar ConversacionesUsuario")
                    }
                }
            }
        }
    }
}

@Composable
fun SupabasePostsCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf(emptyList<Post>()) }
    var senderId by remember { mutableStateOf("") }
    var receiverId by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    fun cargarPosts() {
        scope.launch {
            repository.getAll<Post>("posts").collect { posts = it }
        }
    }

    LaunchedEffect(Unit) { cargarPosts() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Posts",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Posts registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(posts) { post ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Post: ${post.content} \nFrom: ${post.senderId} to ${post.receiverId}")
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<Post>("posts", "idPost", post.idPost)
                                        cargarPosts()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = senderId,
                        onValueChange = { senderId = it },
                        label = { Text("Sender ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = receiverId,
                        onValueChange = { receiverId = it },
                        label = { Text("Receiver ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                repository.addItem(
                                    "posts",
                                    Post(
                                        senderId = senderId,
                                        receiverId = receiverId,
                                        content = content
                                    )
                                )
                                senderId = ""
                                receiverId = ""
                                content = ""
                                cargarPosts()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Post")
                    }
                }
            }
        }
    }
}

@Composable
fun SupabaseHilosCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var hilos by remember { mutableStateOf(emptyList<Hilo>()) }
    var nombre by remember { mutableStateOf("") }
    // Para simplificar, asumimos que la lista de foreros se ingresa como una cadena separada por comas.
    var foreros by remember { mutableStateOf("") }

    fun cargarHilos() {
        scope.launch {
            repository.getAll<Hilo>("hilos").collect { hilos = it }
        }
    }

    LaunchedEffect(Unit) { cargarHilos() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Hilos",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Hilos registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(hilos) { hilo ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Hilo: ${hilo.nombre ?: "Sin nombre"} \nForeros: ${hilo.idForeros.joinToString()}")
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<Hilo>("hilos", "idHilo", hilo.idHilo)
                                        cargarHilos()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Hilo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = foreros,
                        onValueChange = { foreros = it },
                        label = { Text("Foreros (separados por comas)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                val listaForeros = foreros.split(",").map { it.trim() }
                                repository.addItem(
                                    "hilos",
                                    Hilo(
                                        idForeros = listaForeros,
                                        posts = emptyList(),
                                        nombre = nombre
                                    )
                                )
                                nombre = ""
                                foreros = ""
                                cargarHilos()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Hilo")
                    }
                }
            }
        }
    }
}

@Composable
fun SupabaseTemasCRUD(navHostController: NavHostController) {
    val repository = SupabaseRepository()
    val scope = rememberCoroutineScope()
    var temas by remember { mutableStateOf(emptyList<Tema>()) }
    var idUsuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    fun cargarTemas() {
        scope.launch {
            repository.getAll<Tema>("temas").collect { temas = it }
        }
    }

    LaunchedEffect(Unit) { cargarTemas() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Temas",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Temas registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(temas) { tema ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tema: ${tema.nombre} - Usuario: ${tema.idUsuario}")
                                Button(onClick = {
                                    scope.launch {
                                        repository.deleteItem<Tema>("temas", "idTema", tema.idTema)
                                        cargarTemas()
                                    }
                                }) { Text("Eliminar") }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = idUsuario,
                        onValueChange = { idUsuario = it },
                        label = { Text("ID Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Tema") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                repository.addItem(
                                    "temas",
                                    Tema(
                                        idUsuario = idUsuario,
                                        nombre = nombre,
                                        hilos = emptyList()
                                    )
                                )
                                idUsuario = ""
                                nombre = ""
                                cargarTemas()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Tema")
                    }
                }
            }
        }
    }
}


@Composable
fun SupabaseUsuariosCRUD(navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    // Variable para almacenar la lista de usuarios
    var usuarios by remember { mutableStateOf(emptyList<Supausuario>()) }
    // Variables para el formulario de inserción
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Variables para editar
    var showEditDialog by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<Supausuario?>(null) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoEmail by remember { mutableStateOf("") }
    var nuevoPassword by remember { mutableStateOf("") }

    // Función para recargar usuarios
    suspend fun cargarUsuarios() {
        usuarios = supabaseClient
            .from("supausuarios")
            .select()
            .decodeList<Supausuario>()
    }

    // Cargar usuarios al iniciar el Composable
    LaunchedEffect(Unit) {
        cargarUsuarios()
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Usuarios",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = modifier.fillMaxSize().padding(padding)) {
                    Text("Supausuarios registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(usuarios) { usuario ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${usuario.nombre}, ${usuario.email}",
                                    style = MaterialTheme.typography.body1
                                )
                                Row {
                                    Button(
                                        onClick = {
                                            // Preparamos los campos para editar
                                            usuarioAEditar = usuario
                                            nuevoNombre = usuario.nombre
                                            nuevoEmail = usuario.email
                                            nuevoPassword = usuario.password
                                            showEditDialog = true
                                        },
                                        modifier = Modifier.padding(end = 4.dp)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                eliminarUsuario(usuario.id)
                                                cargarUsuarios()
                                            }
                                        }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Insertar nuevo usuario", style = MaterialTheme.typography.h6)
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                insertarUsuario(nombre, email, password)
                                // Limpiar campos y recargar la lista
                                nombre = ""
                                email = ""
                                password = ""
                                cargarUsuarios()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Usuario")
                    }
                }
            }

            // Diálogo para editar usuario
            if (showEditDialog && usuarioAEditar != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Editar usuario") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = nuevoNombre,
                                onValueChange = { nuevoNombre = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = nuevoEmail,
                                onValueChange = { nuevoEmail = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = nuevoPassword,
                                onValueChange = { nuevoPassword = it },
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                scope.launch {
                                    usuarioAEditar?.let { usuario ->
                                        actualizarUsuario(
                                            usuario.id,
                                            nuevoNombre,
                                            nuevoEmail,
                                            nuevoPassword
                                        )
                                        cargarUsuarios()
                                        showEditDialog = false
                                    }
                                }
                            }
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

// Función para insertar usuario (como ya lo tienes)
suspend fun insertarUsuario(nombre: String, correo: String, contrasennia: String) {
    // Usamos signInAnonymously para fines de prueba (o puedes integrar Auth)
    supabaseClient.auth.signInAnonymously(data = null, captchaToken = null)
    val nuevoUsuario = Supausuario(
        nombre = nombre,
        email = correo,
        password = contrasennia
    )
    try {
        supabaseClient
            .from("supausuarios")
            .insert(nuevoUsuario)
            .decodeList<Supausuario>().run {
                println("Usuario insertado: $this")
            }
    } catch (e: Exception) {
        if (e.message?.contains("Expected start of the array") == true) {
            // Se ignora si la respuesta está vacía
        } else {
            throw e
        }
    }
}

suspend fun actualizarUsuario(id: Int, nuevoNombre: String, nuevoEmail: String, nuevoPassword: String) {
    val updateData = mapOf(
        "nombre" to nuevoNombre,
        "email" to nuevoEmail,
        "password" to nuevoPassword
    )
    try {
        supabaseClient
            .from("supausuarios")
            .update(updateData) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeList<Supausuario>()
            .run {
                println("Usuario actualizado: $this")
            }
    } catch (e: Exception) {
        throw Exception("Error actualizando usuario: ${e.message}")
    }
}

suspend fun eliminarUsuario(id: Int) {
    try {
        val response = supabaseClient
            .from("supausuarios")
            .delete {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeList<Supausuario>()

        if (response.isNotEmpty()) {
            println("Usuario eliminado: $response")
        } else {
            println("No se encontró ningún usuario con el ID proporcionado.")
        }
    } catch (e: Exception) {
        throw Exception("Error eliminando usuario: ${e.message}")
    }
}

@Serializable
data class Supausuario(
    val id: Int = 0,  // Se espera que sea autogenerado por Supabase
    val nombre: String,
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return "Usuario(id='$id', nombre='$nombre', email='$email', password='$password')"
    }
}