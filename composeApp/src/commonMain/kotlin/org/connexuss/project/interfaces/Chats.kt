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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.UsuariosPreCreados
import org.connexuss.project.misc.Imagen
import org.jetbrains.compose.resources.painterResource

/**
 * Muestra el chat entre dos usuarios.
 *
 * Busca la conversación por su identificador y muestra los mensajes intercambiados.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación.
 */
@Composable
fun mostrarChat(navController: NavHostController, chatId : String?) {
    // Obtiene la lista de conversaciones y busca la que tenga el id pasado

    val listaChats = UsuarioPrincipal?.getChatUser()?.conversaciones
    val chat = listaChats?.find { it == chatId } ?: return

    val otherParticipantId = chat.participants.find { it != UsuarioPrincipal?.getIdUnico() }
        ?: chat.participants.getOrNull(1) ?: ""

    val otherParticipantUser = UsuariosPreCreados.find { it.getIdUnico() == otherParticipantId }
    val otherParticipantName = UsuariosPreCreados.find { it.getIdUnico() == otherParticipantId }?.getNombreCompleto() ?: otherParticipantId

    val profileImage = otherParticipantUser?.getImagenPerfil() ?: Res.drawable.connexus

    var mensajeNuevo by remember { mutableStateOf("") }
    val messagesState = remember { mutableStateListOf<Mensaje>().apply { addAll(chat.messages) } }

    Scaffold(
        topBar = {
            TopBarUsuario(
                title = otherParticipantName, // Muestra el nombre del otro participante
                profileImage = profileImage,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false,
                onTitleClick = {
                    // Navega al perfil del otro participante
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
            // Sección de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messagesState) { mensaje ->
                    // Dependiendo del senderId, izquierda o derecha
                    val isParticipant1 = mensaje.senderId == otherParticipantId
                    if (isParticipant1) Alignment.CenterStart else Alignment.CenterEnd

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = if (isParticipant1) Alignment.CenterStart else Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .widthIn(max = 250.dp)
                                .background(if (isParticipant1) Color(0xFFB2EBF2) else Color(0xFFC8E6C9))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Text(text = mensaje.content,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    style = MaterialTheme.typography.body1)
                            }
                        }
                    }
                }
            }

            // Barra de escritura
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

                        if (mensajeNuevo.isNotBlank()) {
                            val newMessage = UsuarioPrincipal?.let {
                                Mensaje(
                                    senderId = it.getIdUnico(),
                                    receiverId = otherParticipantId,
                                    content = mensajeNuevo,
                                )
                            }
                            if (newMessage != null) {
                                messagesState.add(newMessage)
                            }

                            // Actualiza la conversación
                            val updatedConversation = chat.copy(messages = messagesState.toList())

                            // Actualiza la conversación en UsuarioPrincipal
                            val convsPrincipal =
                                UsuarioPrincipal?.getChatUser()!!.conversaciones.toMutableList()
                            val indexPrincipal = convsPrincipal.indexOfFirst { it.id == chat.id }
                            if (indexPrincipal != -1) {
                                convsPrincipal[indexPrincipal] = updatedConversation
                                UsuarioPrincipal!!.setChatUser(
                                    UsuarioPrincipal!!.getChatUser()!!
                                        .copy(conversaciones = convsPrincipal)
                                )
                            }

                            // Actualiza la conversación en el otro usuario, si lo encuentra
                            UsuariosPreCreados.find { it.getIdUnico() == otherParticipantId }
                                ?.let { otherUser ->
                                    val convsOther =
                                        otherUser.getChatUser()?.conversaciones?.toMutableList()
                                    val indexOther = convsOther?.indexOfFirst { it.id == chat.id }
                                    if (indexOther != -1) {
                                        if (indexOther != null) {
                                            convsOther[indexOther] = updatedConversation
                                        }
                                        otherUser.getChatUser()?.let {
                                            convsOther?.let { it1 ->
                                                it
                                                    .copy(conversaciones = it1)
                                            }?.let { it2 ->
                                                otherUser.setChatUser(
                                                    it2
                                                )
                                            }
                                        }
                                    }
                                }

                            mensajeNuevo = ""
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

/**
 * Muestra el chat grupal.
 *
 * Busca la conversación grupal por su identificador y muestra los mensajes compartidos.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación grupal.
 * @param imagenesPerfil Lista de imágenes de perfil de los participantes.
 */
@Composable
fun mostrarChatGrupo(navController: NavHostController, chatId: String?, imagenesPerfil: List<Imagen>) {
    val listaChats = UsuarioPrincipal?.getChatUser()?.conversaciones
    val chat = listaChats?.find { it.id == chatId } ?: return
    val idUsuario = UsuarioPrincipal?.getIdUnico()
    val groupTitle = if (!chat.nombre.isNullOrBlank()) chat.nombre else chat.id

    var mensajeNuevo by remember { mutableStateOf("") }
    val messagesState = remember { mutableStateListOf<Mensaje>().apply { addAll(chat.messages) } }

    Scaffold(
        topBar = {
            TopBarGrupo(
                title = groupTitle,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true,
                onUsuariosClick = {
                    // Al pulsar, abre una pantalla con una lista de los participantes del grupo
                    //navController.navigate("mostrarParticipantesGrupo/$chatId")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sección de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messagesState) { mensaje ->
                    // Buscamos el usuario emisor en UsuariosPreCreados
                    val senderUser = UsuariosPreCreados.find { it.getIdUnico() == mensaje.senderId }
                    val senderAlias = senderUser?.getAlias() ?: mensaje.senderId
                    // Obtenemos la imagen de perfil, o usamos la imagen por defecto
                    val senderImageRes = senderUser?.getImagenPerfil() ?: Res.drawable.connexus
                    val imagePainter = painterResource(senderImageRes)

                    // Determinamos si el mensaje es del UsuarioPrincipal
                    val vaDerecha = idUsuario == mensaje.senderId

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (vaDerecha) Arrangement.End else Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!vaDerecha) {
                            // Imagen del emisor a la izquierda: la envolvemos en clickable para navegar a su perfil
                            Image(
                                painter = imagePainter,
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                                    .clickable {
                                        // Navega al perfil del emisor
                                        navController.navigate("mostrarPerfilUsuario/${senderUser?.getIdUnico() ?: mensaje.senderId}")
                                    }
                            )
                        }

                        // Caja de mensaje
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = if (vaDerecha) 0.dp else 8.dp,
                                    end = if (vaDerecha) 8.dp else 0.dp
                                )
                                .widthIn(max = 250.dp)
                                .background(
                                    color = if (vaDerecha) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (vaDerecha) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
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

                        if (vaDerecha) {
                            // Imagen del emisor a la derecha (generalmente el UsuarioPrincipal, pero la hacemos clickable igual)
                            Image(
                                painter = imagePainter,
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 8.dp)
                                    .clickable {
                                        navController.navigate("mostrarPerfilUsuario/${senderUser?.getIdUnico() ?: mensaje.senderId}")
                                    }
                            )
                        }
                    }
                }
            }

            // Barra de escritura
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
                        if (mensajeNuevo.isNotBlank()) {
                            val newMessage = UsuarioPrincipal?.let {
                                Mensaje(
                                    senderId = it.getIdUnico(),
                                    receiverId = "", // En grupo no se usa este campo
                                    content = mensajeNuevo,
                                )
                            }
                            if (newMessage != null) {
                                messagesState.add(newMessage)
                            }

                            val updatedConversation = chat.copy(messages = messagesState.toList())

                            // Actualiza la conversación en UsuarioPrincipal
                            val convsPrincipal = UsuarioPrincipal?.getChatUser()!!.conversaciones.toMutableList()
                            val indexPrincipal = convsPrincipal.indexOfFirst { it.id == chat.id }
                            if (indexPrincipal != -1) {
                                convsPrincipal[indexPrincipal] = updatedConversation
                                UsuarioPrincipal!!.setChatUser(
                                    UsuarioPrincipal!!.getChatUser()!!.copy(conversaciones = convsPrincipal)
                                )
                            }

                            // Actualiza la conversación para cada participante del grupo
                            chat.participants.forEach { participantId ->
                                UsuariosPreCreados.find { it.getIdUnico() == participantId }?.let { otherUser ->
                                    val convsOther = otherUser.getChatUser()?.conversaciones?.toMutableList()
                                    val indexOther = convsOther?.indexOfFirst { it.id == chat.id }
                                    if (indexOther != -1) {
                                        if (indexOther != null) {
                                            convsOther[indexOther] = updatedConversation
                                        }
                                        otherUser.getChatUser()?.let {
                                            convsOther?.let { it1 -> it.copy(conversaciones = it1) }
                                                ?.let { it2 ->
                                                    otherUser.setChatUser(
                                                        it2
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                            mensajeNuevo = ""
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