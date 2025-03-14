package org.connexuss.project.interfaces

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