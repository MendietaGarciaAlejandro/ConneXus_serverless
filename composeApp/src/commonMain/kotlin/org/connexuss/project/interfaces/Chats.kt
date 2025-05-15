package org.connexuss.project.interfaces

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.instanciaSupabaseClient
import org.connexuss.project.supabase.subscribeTableAsFlow
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.DrawableResource

/**
 * Muestra el chat entre dos usuarios.
 *
 * Busca la conversación por su identificador y muestra los mensajes intercambiados.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación.
 */
@Composable
fun mostrarChat(navController: NavHostController, chatId: String?) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val supabaseClient = remember {
        instanciaSupabaseClient(
            tieneStorage = true,
            tieneAuth = true,
            tieneRealtime = true,
            tienePostgrest = true
        )
    }
    val scope = rememberCoroutineScope()

    var participantes by remember { mutableStateOf<List<String>>(emptyList()) }
    var otroUsuarioNombre by remember { mutableStateOf<String?>(null) }
    var otroUsuarioImagen by remember { mutableStateOf<DrawableResource?>(null) }
    var mensajeNuevo by remember { mutableStateOf("") }

    val todosLosMensajes by supabaseClient
        .subscribeTableAsFlow<Mensaje, String>(
            table = "mensaje",
            primaryKey = Mensaje::id,
            filter = null
        )
        .collectAsState(initial = emptyList())

    val mensajes = todosLosMensajes.filter { it.idconversacion == chatId }

    LaunchedEffect(chatId) {
        if (chatId == null) return@LaunchedEffect
        val repo = SupabaseRepositorioGenerico()

        val relaciones = repo.getAll<ConversacionesUsuario>("conversaciones_usuario").first()
        participantes = relaciones
            .filter { it.idconversacion == chatId }
            .map { it.idusuario }

        println("👥 Participantes cargados: $participantes")

        val otroUsuarioId = participantes.firstOrNull { it != currentUserId }
        if (otroUsuarioId != null) {
            val todosUsuarios = repo.getAll<Usuario>("usuario").first()
            val otroUsuario = todosUsuarios.find { it.getIdUnicoMio() == otroUsuarioId }
            otroUsuarioNombre = otroUsuario?.getNombreCompletoMio()
            //otroUsuarioImagen = otroUsuario?.getImagenPerfilMio()
            println("🙋 Nombre otro participante: $otroUsuarioNombre")
        }
    }

    if (chatId == null || participantes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val colors = MaterialTheme.colorScheme
    val textColor = if (colors.background.luminance() > 0.5f)
        colors.onBackground    // fondo claro → texto oscuro
    else
        colors.onBackground    // fondo oscuro → texto claro

    Scaffold(
        topBar = {
            TopBarUsuario(
                title = otroUsuarioNombre ?: "Chat",
                profileImage = otroUsuarioImagen ?: Res.drawable.connexus,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false,
                onTitleClick = {
                    val otroUsuarioId = participantes.firstOrNull { it != currentUserId }
                    if (otroUsuarioId != null) {
                        navController.navigate("mostrarPerfilUsuario/$otroUsuarioId")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(mensajes.sortedBy { it.fechaMensaje }) { mensaje ->
                    val esMio = mensaje.idusuario == currentUserId
                    var expanded by remember { mutableStateOf(false) }
                    var showEditDialog by remember { mutableStateOf(false) }
                    var nuevoContenido by remember { mutableStateOf(mensaje.content) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (esMio) expanded = true
                                    }
                                )
                            },
                        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (esMio) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text  = mensaje.content,
                                color = textColor,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        if (esMio) {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Editar") },
                                    onClick = {
                                        nuevoContenido = mensaje.content
                                        showEditDialog = true
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    onClick = {
                                        scope.launch {
                                            supabaseClient
                                                .from("mensaje")
                                                .update( { set("content", "Mensaje eliminado") }) {
                                                    filter { eq("id", mensaje.id) }
                                                }
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }

                        if (showEditDialog) {
                            AlertDialog(
                                onDismissRequest = { showEditDialog = false },
                                title = { Text("Editar mensaje") },
                                text = {
                                    OutlinedTextField(
                                        value = nuevoContenido,
                                        onValueChange = { nuevoContenido = it },
                                        label = { Text("Nuevo contenido") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        scope.launch {
                                            supabaseClient
                                                .from("mensaje")
                                                .update({ set("content", nuevoContenido.trim()) }) {
                                                    filter { eq("id", mensaje.id) }
                                                }
                                            showEditDialog = false
                                        }
                                    }) {
                                        Text("Guardar")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showEditDialog = false }) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }
                    }
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensajeNuevo,
                    onValueChange = { mensajeNuevo = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") }
                )
                IconButton(onClick = {
                    if (mensajeNuevo.isNotBlank()) {
                        scope.launch {
                            val nuevo = Mensaje(
                                content = mensajeNuevo.trim(),
                                idusuario = currentUserId,
                                idconversacion = chatId
                            )
                            supabaseClient.from("mensaje").insert(nuevo)
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}

/**
 * Muestra el chat grupal.
 *
 * Busca la conversación grupal por su identificador y muestra los mensajes compartidos.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación grupal.
 * @param imagenesPerfil Lista de imágenes de perfil de los participantes.
 */
@Composable
fun mostrarChatGrupo(
    navController: NavHostController,
    chatId: String?,
    imagenesPerfil: List<Imagen>   // (No se usa de momento, opcional en el futuro)
) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    var todosUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }


    val supabaseClient = remember {
        instanciaSupabaseClient(
            tieneStorage = true,
            tieneAuth = true,
            tieneRealtime = true,
            tienePostgrest = true
        )
    }
    val scope = rememberCoroutineScope()

    var chatNombre by remember { mutableStateOf<String>("") }
    var mensajeNuevo by remember { mutableStateOf("") }

    val todosLosMensajes by supabaseClient
        .subscribeTableAsFlow<Mensaje, String>(
            table = "mensaje",
            primaryKey = Mensaje::id,
            filter = null
        )
        .collectAsState(initial = emptyList())

    val mensajes = todosLosMensajes.filter { it.idconversacion == chatId }

    LaunchedEffect(chatId) {
        if (chatId == null) return@LaunchedEffect
        val repo = SupabaseRepositorioGenerico()
        todosUsuarios = repo.getAll<Usuario>("usuario").first()

        // Cargamos el nombre del grupo
        val conversaciones = repo.getAll<Conversacion>("conversacion").first()
        chatNombre = conversaciones.find { it.id == chatId }?.nombre ?: "Grupo"
    }

    if (chatId == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopBarGrupo(
                title = chatNombre,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true,
                onUsuariosClick = {
                    navController.navigate("mostrarParticipantesGrupo/$chatId")
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(mensajes.sortedBy { it.fechaMensaje }) { mensaje ->
                    val esMio = mensaje.idusuario == currentUserId
                    val senderAlias = todosUsuarios
                        .find { it.getIdUnicoMio() == mensaje.idusuario }
                        ?.getAliasPrivadoMio() ?: "Usuario"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    if (esMio) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            if (!esMio) {
                                Text(
                                    text = senderAlias,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }
                            Text(text = mensaje.content)
                        }
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensajeNuevo,
                    onValueChange = { mensajeNuevo = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") }
                )
                IconButton(onClick = {
                    if (mensajeNuevo.isNotBlank()) {
                        scope.launch {
                            val nuevo = Mensaje(
                                content = mensajeNuevo.trim(),
                                idusuario = currentUserId,
                                idconversacion = chatId
                            )
                            supabaseClient.from("mensaje").insert(nuevo)
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}


/*
// --- Nuevo Chat ---
@Composable
@Preview
fun muestraNuevoChat() {
    var nombreChat by remember { mutableStateOf("") }
    val participantes = remember { mutableStateListOf<String>() }
    var nuevoParticipante by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("nombre_del_chat"),
                    navController = null,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
                )
            }
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
                    label = { Text(traducir("nombre_del_chat")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nuevoParticipante,
                        onValueChange = { nuevoParticipante = it },
                        label = { Text(traducir("agregar_participante")) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nuevoParticipante.isNotEmpty()) {
                            participantes.add(nuevoParticipante)
                            nuevoParticipante = ""
                        }
                    }) {
                        Text(traducir("agregar"))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(traducir("participantes"), style = MaterialTheme.typography.subtitle1)
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
                    Text(traducir("crear_chat"))
                }
            }
        }
    }
}

// --- Chat Room ---
@Composable
@Preview
fun muestraChatRoom() {
    val mensajes = remember { mutableStateListOf<Mensaje>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("chat_room"),
                    navController = null,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
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
                        label = { Text(traducir("nuevo_mensaje")) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nuevoMensaje.isNotEmpty()) {
                            mensajes.add(
                                Mensaje(
                                    id = Clock.System.now().toEpochMilliseconds().toString(),
                                    senderId = "user", // Aquí usar el ID del usuario autenticado
                                    receiverId = "user", // Aquí usar el ID del destinatario
                                    content = nuevoMensaje,
                                    fechaMensaje = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                )
                            )
                            nuevoMensaje = ""
                        }
                    }) {
                        Text(traducir("enviar"))
                    }
                }
            }
        }
    }
}

// --- Chats ---
@Composable
@Preview
fun muestraChats() {
    val chats = remember { mutableStateListOf<Conversacion>() }
    var nuevoChat by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("chats"),
                    navController = null,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
                )
            }
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
                    label = { Text(traducir("nuevo_chat")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nuevoChat.isNotEmpty()) {
                            chats.add(Conversacion(id = nuevoChat, participants = listOf("user1", "user2")))
                            nuevoChat = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(traducir("agregar_chat"))
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
                                Text(traducir("chat") + ": ${chat.id}", style = MaterialTheme.typography.subtitle1)
                                Text(traducir("participantes_chat") + "${chat.participants.joinToString(", ")}", style = MaterialTheme.typography.body2)
                            }
                        }
                    }
                }
            }
        }
    }
}
 */