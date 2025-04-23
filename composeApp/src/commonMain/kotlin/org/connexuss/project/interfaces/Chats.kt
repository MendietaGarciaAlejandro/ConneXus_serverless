package org.connexuss.project.interfaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.UsuariosPreCreados
import org.connexuss.project.misc.Imagen
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.jetbrains.compose.resources.painterResource

/**
 * Muestra el chat entre dos usuarios.
 *
 * Busca la conversación por su identificador y muestra los mensajes intercambiados.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación.
 */
@Composable
fun mostrarChat(navController: NavHostController, chatId: String?) {
    // Se asume que el UsuarioPrincipal ya está definido de forma global
    val currentUser = UsuarioPrincipal
    val currentUserId = currentUser?.getIdUnicoMio() ?: return

    val supabaseRepo = remember { SupabaseRepositorioGenerico() }
    var chat by remember { mutableStateOf<Conversacion?>(null) }
    var participants by remember { mutableStateOf<List<String>>(emptyList()) }
    var messages by remember { mutableStateOf<List<Mensaje>>(emptyList()) }
    var mensajeNuevo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatId) {
        chatId?.let { id ->
            // --- CONSULTA DE CONVERSACIÓN ---
            // Se obtiene la conversación por su id. Se asume que, para este usuario, la conversación existe.
            supabaseRepo.getItem<Conversacion>("conversacion") {
                scope.launch {
                    select {
                        filter {
                            eq("id", id)
                        }
                    }
                }
            }.collect { conv ->
                chat = conv
            }

            // --- PARTICIPANTES ---
            // Se obtiene la lista de relaciones en conversaciones_usuario filtrando por el chatId.
            supabaseRepo.getAll<ConversacionesUsuario>("conversaciones_usuario")
                .collect { convUsuarios ->
                    // Se considera solo las relaciones del usuario actual
                    participants = convUsuarios
                        .filter { it.idconversacion == id }
                        .map { it.idusuario }
                        .distinct()
                    // Si no existe la relación con el UsuarioPrincipal, se sale
                    if (currentUserId !in participants) {
                        // Podrías mostrar un error o redireccionar
                        return@collect
                    }
                }

            // --- MENSAJES ---
            // Se obtienen todos los mensajes de la conversación
            supabaseRepo.getAll<Mensaje>("mensaje")
                .collect { allMessages ->
                    messages = allMessages.filter { it.idconversacion == id }
                }
        }
    }

    // Determinar al otro participante (se asume que siempre son 2 participantes)
    val otherParticipantId = participants.firstOrNull { it != currentUserId } ?: ""
    val otherParticipant = UsuariosPreCreados.find { it.getIdUnicoMio() == otherParticipantId }
    val otherParticipantName = otherParticipant?.getNombreCompletoMio() ?: otherParticipantId
    val profileImage = otherParticipant?.getImagenPerfilMio() ?: Res.drawable.connexus

    Scaffold(
        topBar = {
            TopBarUsuario(
                title = otherParticipantName,
                profileImage = profileImage,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false,
                onTitleClick = {
                    // Navega al perfil del otro usuario
                    navController.navigate("mostrarPerfilUsuario/$otherParticipantId")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // --- LISTA DE MENSAJES ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages) { mensaje ->
                    val isCurrentUser = mensaje.idusuario == currentUserId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isCurrentUser) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = mensaje.content,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }

            // --- ENVIAR MENSAJE ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensajeNuevo,
                    onValueChange = { mensajeNuevo = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = traducir("escribe_mensaje")) }
                )
                IconButton(
                    onClick = {
                        if (mensajeNuevo.isNotBlank() && chatId != null) {
                            scope.launch {
                                // Crea y envía el nuevo mensaje, vinculándolo a la conversación y al usuario actual
                                val newMessage = Mensaje(
                                    content = mensajeNuevo,
                                    idusuario = currentUserId,
                                    idconversacion = chatId
                                )
                                // Se agrega el mensaje a la BD
                                supabaseRepo.addItem("mensaje", newMessage)
                                mensajeNuevo = ""
                                // Se refrescan los mensajes actualizando el estado
                                supabaseRepo.getAll<Mensaje>("mensaje")
                                    .collect { allMessages ->
                                        messages = allMessages.filter { it.idconversacion == chatId }
                                    }
                            }
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = traducir("enviar"))
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
    imagenesPerfil: List<Imagen>
) {
    val currentUser = UsuarioPrincipal
    val currentUserId = currentUser?.getIdUnicoMio() ?: return

    val supabaseRepo = remember { SupabaseRepositorioGenerico() }
    var chat by remember { mutableStateOf<Conversacion?>(null) }
    var messages by remember { mutableStateOf<List<Mensaje>>(emptyList()) }
    var mensajeNuevo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Recupera datos de la conversación y sus mensajes a través del repositorio
    LaunchedEffect(chatId) {
        chatId?.let { id ->
            supabaseRepo.getItem<Conversacion>("conversacion") {
                scope.launch {
                    select {
                        filter {
                            eq("id", id)
                        }
                    }
                }
            }.collect { conv ->
                chat = conv
            }

            supabaseRepo.getAll<Mensaje>("mensaje")
                .collect { allMessages ->
                    messages = allMessages.filter { it.idconversacion == id }
                }
        }
    }

    // El título del grupo se define a partir del nombre o del id de la conversación
    val groupTitle = chat?.nombre?.takeIf { it.isNotBlank() } ?: chatId.orEmpty()

    Scaffold(
        topBar = {
            TopBarGrupo(
                title = groupTitle,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true,
                onUsuariosClick = {
                    // Navega a la pantalla de participantes si se requiere:
                    // navController.navigate("mostrarParticipantesGrupo/$chatId")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages) { mensaje ->
                    // Se utiliza 'idusuario' del mensaje para determinar el remitente
                    val isCurrentUser = mensaje.idusuario == currentUserId
                    val senderUser = UsuariosPreCreados.find { it.getIdUnicoMio() == mensaje.idusuario }
                    val senderAlias = senderUser?.getAliasMio() ?: mensaje.idusuario
                    val senderImageRes = senderUser?.getImagenPerfilMio() ?: Res.drawable.connexus
                    val imagePainter = painterResource(senderImageRes)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isCurrentUser) {
                            // Imagen del remitente a la izquierda, navegable a su perfil
                            Image(
                                painter = imagePainter,
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                                    .clickable {
                                        navController.navigate(
                                            "mostrarPerfilUsuario/${senderUser?.getIdUnicoMio() ?: mensaje.idusuario}"
                                        )
                                    }
                            )
                        }

                        // Caja con el mensaje y el alias del remitente
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = if (isCurrentUser) 0.dp else 8.dp,
                                    end = if (isCurrentUser) 8.dp else 0.dp
                                )
                                .widthIn(max = 250.dp)
                                .background(
                                    color = if (isCurrentUser) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isCurrentUser) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = senderAlias,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    style = MaterialTheme.typography.caption
                                )
                                Text(
                                    text = mensaje.content,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }

                        if (isCurrentUser) {
                            // Imagen del remitente (UsuarioPrincipal) a la derecha
                            Image(
                                painter = imagePainter,
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 8.dp)
                                    .clickable {
                                        navController.navigate(
                                            "mostrarPerfilUsuario/${senderUser?.getIdUnicoMio() ?: mensaje.idusuario}"
                                        )
                                    }
                            )
                        }
                    }
                }
            }

            // Barra de entrada para escribir un mensaje nuevo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensajeNuevo,
                    onValueChange = { mensajeNuevo = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = traducir("escribe_mensaje")) }
                )
                IconButton(
                    onClick = {
                        if (mensajeNuevo.isNotBlank() && chatId != null) {
                            scope.launch {
                                // Se crea el nuevo mensaje usando 'idusuario' y 'idconversacion'
                                val newMessage = Mensaje(
                                    content = mensajeNuevo,
                                    idusuario = currentUserId,
                                    idconversacion = chatId
                                )
                                supabaseRepo.addItem("mensaje", newMessage)
                                mensajeNuevo = ""
                                // Actualiza la lista de mensajes de la conversación
                                supabaseRepo.getAll<Mensaje>("mensaje")
                                    .collect { allMessages ->
                                        messages = allMessages.filter { it.idconversacion == chatId }
                                    }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = traducir("enviar")
                    )
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