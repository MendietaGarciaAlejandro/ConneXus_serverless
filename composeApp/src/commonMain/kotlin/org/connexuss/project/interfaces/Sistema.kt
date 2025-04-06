package org.connexuss.project.interfaces

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.ic_chats
import connexus_serverless.composeapp.generated.resources.ic_foros
import connexus_serverless.composeapp.generated.resources.usuarios
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.connexuss.project.actualizarUsuariosGrupoGeneral
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.firebase.FirestoreUsuariosNuestros
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.UsuariosPreCreados
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DefaultTopBar(
    title: String, // Se pasa la clave en lugar del texto literal
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Se usa traducir para obtener el texto a partir de la clave
                Text(text = traducir(title))
            }
        },
        navigationIcon = if (showBackButton) {
            {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        // Se obtiene el texto traducido para "atrás"
                        contentDescription = traducir("atras")
                    )
                }
            }
        } else null,
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = {
                    navController?.navigate("ajustes")
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        // Se obtiene el texto traducido para "ajustes"
                        contentDescription = traducir("ajustes")
                    )
                }
            }
        }
    )
}

// Topbar para el grupo en el que se esta chateando,mostrando a la derecha un icono de usuarios
@Composable
fun TopBarGrupo(
    title: String, // Clave para el título (se usará traducir(title))
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
    onUsuariosClick: () -> Unit = {} // Acción al pulsar sobre el icono de usuarios
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Título traducido
                Text(text = traducir(title))
            }
        },
        navigationIcon = if (showBackButton) {
            {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                      //  navController.navigate("usuariosGrupo")
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = traducir("atras")
                    )
                }
            }
        } else null,
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = onUsuariosClick) {
                    Icon(
                        painter = painterResource(Res.drawable.usuarios),
                        contentDescription = traducir("usuarios"),
                        // Hacemos que tenga un tamaño de 24dp
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

// Interfaz que muestra los usuarios del grupo, si se hace clic en un usuario, se muestra su perfil
@Composable
fun MuestraUsuariosGrupo(
    usuarios: List<Usuario>,
    navController: NavHostController
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("usuarios_grupo"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            // Usa LimitaTamanioAncho para restringir el ancho en pantallas grandes
            LimitaTamanioAncho { modifier ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    items(usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    navController.navigate("mostrarPerfil/${usuario.getIdUnico()}")
                                },
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${traducir("nombre_label")} ${usuario.getNombreCompleto()}",
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Text(
                                    text = "${traducir("alias_label")} ${usuario.getAlias()}",
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = "${traducir("alias_privado_label")} ${usuario.getAliasPrivado()}",
                                    style = MaterialTheme.typography.body2
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

//TopBar para mostrar el usuario con el que se esta chateando
@Composable
fun TopBarUsuario(
    title: String, // Clave para el título (se usará traducir(title))
    profileImage: DrawableResource, // Imagen del usuario
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
    onTitleClick: () -> Unit = {} // Acción al pulsar sobre el título
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTitleClick() }
            ) {
                // Muestra la imagen del usuario (puedes aplicarle clip circular si lo deseas)
                Image(
                    painter = painterResource(profileImage),
                    contentDescription = traducir("imagen_perfil"),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Título traducido
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = traducir(title), style = MaterialTheme.typography.h6)
                }
            }
        },
        navigationIcon = if (showBackButton) {
            {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = traducir("atras")
                    )
                }
            }
        } else null,
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = {
                    navController?.navigate("ajustes")
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = traducir("ajustes")
                    )
                }
            }
        }
    )
}


//BottomBar
@Composable
fun MiBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation {
        // Ítem de Chats
        BottomNavigationItem(
            selected = currentRoute == "contactos",
            onClick = {
                navController.navigate("contactos") {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_chats),
                    contentDescription = traducir("chats"),
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text(traducir("chats")) }
        )

        // Ítem de Foros
        BottomNavigationItem(
            selected = currentRoute == "foroLocal"/*"foro"*/,
            onClick = {
                navController.navigate("foroLocal"/*"foro"*/) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_foros),
                    contentDescription = traducir("foro"),
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text(traducir("foro")) }
        )
    }
}

// --- Muestra Usuarios ---
@Composable
@Preview
fun muestraUsuarios(navController: NavHostController) {
    val almacenamientoUsuario = remember { AlmacenamientoUsuario() }
    val usuarios = remember { mutableStateListOf<Usuario>() }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val user1 = UtilidadesUsuario().instanciaUsuario(
                "Juan Perez",
                "paco@jerte.org",
                "pakito58",
                true
            )
            val user2 =
                UtilidadesUsuario().instanciaUsuario("Maria Lopez", "marii@si.se", "marii", true)
            val user3 = UtilidadesUsuario().instanciaUsuario(
                "Pedro Sanchez",
                "roba@espannoles.es",
                "roba",
                true
            )
            if (user1 != null) {
                almacenamientoUsuario.agregarUsuario(user1)
            }
            if (user2 != null) {
                almacenamientoUsuario.agregarUsuario(user2)
            }
            if (user3 != null) {
                almacenamientoUsuario.agregarUsuario(user3)
            }
            usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "usuarios", // Se utiliza la clave "usuarios" definida en el mapa
                    navController = navController,
                    showBackButton = true
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
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { showContent = !showContent }) {
                            Text(traducir("mostrar_usuarios")) // Se usa la clave para "Mostrar Usuarios"
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
                                            Text(
                                                text = "${traducir("nombre_label")} ${usuario.getNombreCompleto()}",
                                                style = MaterialTheme.typography.subtitle1
                                            )
                                            Text(
                                                text = "${traducir("alias_label")} ${usuario.getAlias()}",
                                                style = MaterialTheme.typography.body1
                                            )
                                            Text(
                                                text = "${traducir("activo_label")} ${usuario.getActivo()}",
                                                style = MaterialTheme.typography.body2
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- elemento chat ---
//Muestra el id del usuarioPrincipal ya que no esta incluido en la lista de usuarios precreados
@Composable
fun ChatCard(conversacion: Conversacion, navController: NavHostController) {
    val displayName =
        if (!conversacion.nombre.isNullOrBlank()) conversacion.nombre else conversacion.id

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                if (conversacion.grupo) {
                    // Buscamos los usuarios de esa conversación y llenamos una lista con sus idUnico
                    val usuarios =
                        UsuariosPreCreados.filter { it.getIdUnico() in conversacion.participants }

                    // Actualizamos la lista de usuariosGrupoGeneral con los usuarios de la conversación
                    // (excluyendo al UsuarioPrincipal)
                    actualizarUsuariosGrupoGeneral(usuarios.filter { it.getIdUnico() != UsuarioPrincipal?.getIdUnico() })
                    navController.navigate("mostrarChatGrupo/${conversacion.id}")
                } else {
                    navController.navigate("mostrarChat/${conversacion.id}")
                }
            },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Chat: $displayName")
            Text(text = "Participantes: ${conversacion.participants.joinToString()}")
            Text(text = "Número de mensajes: ${conversacion.messages.size}")
        }
    }
}

// --- Chats PorDefecto ---
@Composable
fun muestraChats(navController: NavHostController) {
    val listaChats = UsuarioPrincipal?.getChatUser()?.conversaciones

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("chats"),
                    navController = navController,
                    showBackButton = false,
                    irParaAtras = false,
                    muestraEngranaje = true
                )
            },
            bottomBar = { MiBottomBar(navController) }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Usa items para iterar sobre la lista de chats
                    listaChats?.let { chats ->
                        items(chats) { conversacion ->
                            ChatCard(conversacion = conversacion, navController = navController)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate("nuevo") },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = traducir("nuevo_chat")
                        )
                    }
                }
            }
        }
    }
}

// --- Contactos ---
@Composable
@Preview
fun muestraContactos(navController: NavHostController) {
    // Creamos un estado para la lista de IDs de contactos basado en UsuarioPrincipal.
    val contactosState = remember {
        mutableStateListOf<String>().apply {
            if (UsuarioPrincipal != null) {
                UsuarioPrincipal!!.getContactos()?.let { addAll(it) }
            }
        }
    }
    // Lista completa de usuarios precreados
    val todosLosUsuarios = UsuariosPreCreados
    // Filtramos los usuarios usando el estado
    val usuarios = todosLosUsuarios.filter { it.getIdUnico() in contactosState }

    var showContactoDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    var showChatDialog by remember { mutableStateOf(false) }
    // Estado para los contactos seleccionados para el chat.
    val selectedContacts = remember { mutableStateListOf<Usuario>() }
    // Estado para el nombre del chat de grupo (se mostrará si se selecciona más de 1 contacto)
    var groupChatName by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("contactos"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp)
                ) {
                    LimitaTamanioAncho { modifier ->
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(usuarios) { usuario ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = 4.dp
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "${traducir("nombre_label")} ${usuario.getNombreCompleto()}",
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                        Text(
                                            text = "${traducir("alias_label")} ${usuario.getAlias()}",
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .widthIn(max = 800.dp)
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { showContactoDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = traducir("nuevo_contacto"))
                    }
                    Button(
                        onClick = { showChatDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = traducir("nuevo_chat"))
                    }
                }
                // AlertDialog para "Nuevo Contacto"
                if (showContactoDialog) {
                    AlertDialog(
                        onDismissRequest = { showContactoDialog = false },
                        title = { Text(text = traducir("nuevo_contacto")) },
                        text = {
                            Column {
                                Text("Introduce el idUnico del usuario:")
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    label = { Text("idUnico") }
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val nuevoContactoId = inputText.trim()
                                    // Busca en UsuariosPreCreados si existe un usuario con ese idUnico
                                    val userFound =
                                        UsuariosPreCreados.find { it.getIdUnico() == nuevoContactoId }
                                    if (userFound != null) {
                                        val updatedContacts = UsuarioPrincipal?.getContactos()
                                            ?.toMutableList()
                                        if (updatedContacts != null) {
                                            if (nuevoContactoId !in updatedContacts) {
                                                updatedContacts.add(nuevoContactoId)
                                                UsuarioPrincipal?.setContactos(updatedContacts)
                                                // Actualiza el estado local para recomponer la UI
                                                contactosState.clear()
                                                contactosState.addAll(updatedContacts)
                                            }
                                        }
                                    }
                                    inputText = ""
                                    showContactoDialog = false
                                }
                            ) {
                                Text(text = traducir("guardar"))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    inputText = ""
                                    showContactoDialog = false
                                }
                            ) {
                                Text(text = traducir("cancelar"))
                            }
                        }
                    )
                }
                // AlertDialog para "Nuevo Chat"
                if (showChatDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showChatDialog = false
                            selectedContacts.clear()
                            groupChatName = ""
                        },
                        title = { Text(text = traducir("nuevo_chat")) },
                        text = {
                            Column {
                                Text(text = traducir("selecciona_contactos_chat"))
                                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                                    items(usuarios) { usuario ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            val isSelected = selectedContacts.contains(usuario)
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = { checked ->
                                                    if (checked) {
                                                        selectedContacts.add(usuario)
                                                    } else {
                                                        selectedContacts.remove(usuario)
                                                    }
                                                }
                                            )
                                            Text(text = usuario.getNombreCompleto())
                                        }
                                    }
                                }
                                // Si se han seleccionado más de un contacto (grupo), solicita el nombre del grupo
                                if (selectedContacts.size > 1) {
                                    OutlinedTextField(
                                        value = groupChatName,
                                        onValueChange = { groupChatName = it },
                                        label = { Text("Nombre del grupo") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = onClick@{
                                    // Dentro del AlertDialog para "Nuevo Chat" en la confirmButton:
                                    if (selectedContacts.isNotEmpty()) {
                                        // Construimos el conjunto de participantes: el UsuarioPrincipal y los contactos seleccionados.
                                        val participantesSet =
                                            (UsuarioPrincipal?.let { listOf(it.getIdUnico()) }
                                                ?.plus(selectedContacts.map { it.getIdUnico() }))?.toSet()

                                        // Si sólo se ha seleccionado un contacto (chat individual), comprobamos si ya existe una conversación con ese par.
                                        if (selectedContacts.size == 1) {
                                            val existingChat =
                                                UsuarioPrincipal?.getChatUser()?.conversaciones?.find {
                                                    it.participants.toSet() == participantesSet
                                                }
                                            if (existingChat != null) {
                                                // Por ejemplo, podrías mostrar un mensaje Toast o Snackbar indicando que ya existe.
                                                // Aquí simplemente salimos sin crear una nueva conversación:
                                                showChatDialog = false
                                                selectedContacts.clear()
                                                groupChatName = ""
                                                return@onClick
                                            }
                                        }

                                        // Crea la nueva conversación:
                                        val nuevaConversacion = participantesSet?.let {
                                            Conversacion(
                                                participants = it.toList(),  // Conservamos la lista, el orden puede no ser relevante
                                                messages = emptyList(),
                                                nombre = if (selectedContacts.size > 1 && groupChatName.isNotBlank()) groupChatName else null
                                            )
                                        }
                                        // Actualiza el UsuarioPrincipal: agrega la nueva conversación a su lista
                                        val convActualesPrincipal =
                                            UsuarioPrincipal?.getChatUser()?.conversaciones?.toMutableList()
                                        if (nuevaConversacion != null) {
                                            convActualesPrincipal?.add(nuevaConversacion)
                                        }
                                        convActualesPrincipal?.let {
                                            UsuarioPrincipal?.let { it1 ->
                                                ConversacionesUsuario(
                                                    id = UsuarioPrincipal!!.getChatUser()!!.id, // Mantenemos el id existente
                                                    idUser = it1.getIdUnico(),
                                                    conversaciones = it
                                                )
                                            }
                                        }?.let {
                                            UsuarioPrincipal?.setChatUser(
                                                it
                                            )
                                        }
                                        // Actualiza cada usuario seleccionado: agrega la conversación a sus chats
                                        selectedContacts.forEach { usuario ->
                                            val convActuales =
                                                usuario.getChatUser()?.conversaciones?.toMutableList()
                                            if (nuevaConversacion != null) {
                                                convActuales?.add(nuevaConversacion)
                                            }
                                            val nuevasConversacionesUsuario =
                                                usuario.getChatUser()?.let {
                                                    if (convActuales != null) {
                                                        ConversacionesUsuario(
                                                            id = it.id,
                                                            idUser = usuario.getIdUnico(),
                                                            conversaciones = convActuales
                                                        )
                                                    } else {
                                                        null
                                                    }
                                                }
                                            usuario.setChatUser(nuevasConversacionesUsuario)
                                        }
                                    }
                                    showChatDialog = false
                                    selectedContacts.clear()
                                    groupChatName = ""
                                }
                            ) {
                                Text(text = traducir("crear_chat"))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showChatDialog = false
                                    selectedContacts.clear()
                                    groupChatName = ""
                                }
                            ) {
                                Text(text = traducir("cancelar"))
                            }
                        }
                    )
                }
            }
        }
    }
}


// --- elemento usuario ---
/**
 * Composable que muestra la tarjeta de un usuario.
 *
 * @param usuario el objeto Usuario a mostrar.
 * @param onClick acción a ejecutar al hacer clic en la tarjeta.
 */
@Composable
fun UsuCard(usuario: Usuario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen cuadrada a la izquierda
            usuario.getImagenPerfil()?.let { painterResource(it as DrawableResource) }?.let {
                Image(
                    painter = it,
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Información del usuario en una columna
            Column {
                Text(
                    text = "${traducir("nombre_label")} ${usuario.getNombreCompleto()}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "${traducir("alias_publico")} ${usuario.getAlias()}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${traducir("alias_privado")} ${usuario.getAliasPrivado()}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

// --- Ajustes ---
/**
 * Composable que muestra la pantalla de ajustes.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun muestraAjustes(navController: NavHostController = rememberNavController()) {
    val user = UsuarioPrincipal
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "ajustes", // Se usa la clave para "Ajustes"
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
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
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (user != null) {
                            UsuCard(
                                usuario = user,
                                onClick = {
                                    navController.navigate("mostrarPerfilPrincipal")
                                }
                            )
                        }
                        Button(
                            onClick = { navController.navigate("cambiarTema") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("cambiar_modo_oscuro_tema"))
                        }
                        Button(
                            onClick = { navController.navigate("cambiaFuente") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("cambiar_fuente"))
                        }
                        Button(
                            onClick = { navController.navigate("idiomas") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("cambiar_idioma"))
                        }
                        Button(
                            onClick = { /* Eliminar Chats */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("eliminar_chats"))
                        }
                        Button(
                            onClick = { /* Control de Cuentas */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("control_de_cuentas"))
                        }
                        Button(
                            onClick = { navController.navigate("ajustesAyuda") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("ayuda"))
                        }
                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("cerrar_sesion"))
                        }
                    }
                }
            }
        }
    }
}

// --- Mostrar Perdil ---
/**
 * Composable que muestra el perfil del usuario.
 *
 * @param navController controlador de navegación.
 * @param usuarioU usuario a mostrar.
 */
@Composable
fun mostrarPerfil(navController: NavHostController, usuarioU: Usuario?) {
    // Se recibe el usuario
    val usuario = usuarioU

    // Dialogs
    var showDialogNombre by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }
    var showDialogEmail by remember { mutableStateOf(false) }
    var nuevoEmail by remember { mutableStateOf("") }

    // Campos del usuario
    var aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivado() ?: "") }
    var aliasPublico by remember { mutableStateOf(usuario?.getAlias() ?: "") }
    var descripcion by remember { mutableStateOf(usuario?.getDescripcion() ?: "") }
    var contrasennia by remember { mutableStateOf(usuario?.getContrasennia() ?: "") }
    var email by remember { mutableStateOf(usuario?.getCorreo() ?: "") }
    var isNameVisible by remember { mutableStateOf(false) }

    // Estado para la imagen de perfil (para refrescar la UI al cambiarla)
    val imagenPerfilState = remember { mutableStateOf(usuario?.getImagenPerfil()) }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "perfil", // Clave para "Perfil"
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
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
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (usuario == null) {
                            Text(text = traducir("usuario_no_encontrado"))
                        } else {
                            // Sección superior: Imagen de perfil y botón "Cambiar"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen de perfil en un cuadrado
                                Image(
                                    painter = imagenPerfilState.value?.let { painterResource(it) }
                                        ?: painterResource(Res.drawable.avatar),
                                    contentDescription = "Imagen de perfil",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                )
                            }
                            // Botón para cambiar la imagen
                            Button(
                                onClick = {
                                    // Genera una nueva imagen aleatoria y actualiza tanto el usuario como el estado mutable
                                    val nuevaImagen = usuario.generarImagenPerfilRandom()
                                    usuario.setImagenPerfil(nuevaImagen)
                                    imagenPerfilState.value = nuevaImagen
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = traducir("cambiar"))
                            }

                            // Alias
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = aliasPrivado,
                                    onValueChange = { aliasPrivado = it },
                                    label = { Text(text = traducir("alias_privado")) },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = aliasPublico,
                                    onValueChange = { aliasPublico = it },
                                    label = { Text(text = traducir("alias_publico")) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Descripción
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text(text = traducir("descripcion")) },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                            // Contraseña: campo de solo lectura, modificar mediante Dialog
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = contrasennia,
                                    onValueChange = { /* No se edita directamente */ },
                                    label = { Text(text = traducir("nombre_label")) },
                                    modifier = Modifier.weight(1f),
                                    readOnly = true,
                                    visualTransformation = if (isNameVisible)
                                        VisualTransformation.None
                                    else
                                        PasswordVisualTransformation()
                                )
                                IconButton(
                                    onClick = { isNameVisible = !isNameVisible }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        painter = if (isNameVisible)
                                            painterResource(Res.drawable.visibilidadOn)
                                        else
                                            painterResource(Res.drawable.visibilidadOff),
                                        contentDescription = if (isNameVisible)
                                            traducir("ocultar_nombre")
                                        else
                                            traducir("mostrar_nombre")
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        nuevoNombre = contrasennia
                                        showDialogNombre = true
                                    }
                                ) {
                                    Text(text = traducir("modificar"))
                                }
                            }
                            // Email
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = {},
                                    label = { Text(text = traducir("email")) },
                                    readOnly = true,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = {
                                        nuevoEmail = email
                                        showDialogEmail = true
                                    }
                                ) {
                                    Text(text = traducir("modificar"))
                                }
                            }
                            // Botones inferiores
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        usuario.apply {
                                            setAliasPrivado(aliasPrivado)
                                            setAlias(aliasPublico)
                                            setDescripcion(descripcion)
                                            setContrasennia(contrasennia)
                                            setCorreo(email)
                                        }
                                        navController.popBackStack()
                                    }
                                ) {
                                    Text(text = traducir("aplicar"))
                                }
                                Button(
                                    onClick = { navController.popBackStack() }
                                ) {
                                    Text(text = traducir("cancelar"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // AlertDialog para modificar Email
    if (showDialogEmail) {
        AlertDialog(
            onDismissRequest = { showDialogEmail = false },
            title = { Text(text = traducir("modificar_email")) },
            text = {
                OutlinedTextField(
                    value = nuevoEmail,
                    onValueChange = { nuevoEmail = it },
                    label = { Text(text = traducir("nuevo_email")) }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        email = nuevoEmail
                        showDialogEmail = false
                    }
                ) {
                    Text(text = traducir("guardar"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialogEmail = false }
                ) {
                    Text(text = traducir("cancelar"))
                }
            }
        )
    }
    // AlertDialog para modificar Nombre
    if (showDialogNombre) {
        AlertDialog(
            onDismissRequest = { showDialogNombre = false },
            title = { Text(text = traducir("modificar_nombre")) },
            text = {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text(text = traducir("nuevo_nombre")) }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        contrasennia = nuevoNombre
                        showDialogNombre = false
                    }
                ) {
                    Text(text = traducir("guardar"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialogNombre = false }
                ) {
                    Text(text = traducir("cancelar"))
                }
            }
        )
    }
}


//Mostrar perfil usuario chat, por ahora no muestra la imagen del usuario, solo muestra negro
/**
 * Composable que muestra el perfil de un usuario en el chat.
 *
 * @param navController controlador de navegación.
 * @param userId identificador único del usuario.
 * @param imagenesApp lista de imágenes de la aplicación.
 */
@Composable
fun mostrarPerfilUsuario(
    navController: NavHostController,
    userId: String?,
    imagenesApp: List<Imagen>
) {
    // Busca el usuario en tu lista de usuarios (UsuariosPreCreados) según el userId
    val usuario = UsuariosPreCreados.find { it.getIdUnico() == userId }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = usuario?.getNombreCompleto() ?: "Perfil",
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false
            )
        }
    ) { padding ->
        if (usuario == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Usuario no encontrado")
            }
        } else {
            var aliasPrivado by remember { mutableStateOf(usuario.getAliasPrivado()) }
            var aliasPublico by remember { mutableStateOf(usuario.getAlias()) }
            var descripcion by remember { mutableStateOf(usuario.getDescripcion()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de perfil
                usuario.getImagenPerfil()?.let { painterResource(it) }?.let {
                    Image(
                        painter = it,
                        contentDescription = "Imagen de perfil de ${usuario.getNombreCompleto()}",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )
                }

                // Alias Privado
                OutlinedTextField(
                    value = aliasPrivado,
                    onValueChange = { aliasPrivado = it },
                    readOnly = true,
                    label = { Text("Alias Privado") },
                    modifier = Modifier.fillMaxWidth()

                )

                // Alias Público
                OutlinedTextField(
                    value = aliasPublico,
                    readOnly = true,
                    onValueChange = { aliasPublico = it },
                    label = { Text("Alias Público") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Descripción
                OutlinedTextField(
                    value = descripcion,
                    readOnly = true,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // Botones
                Spacer(modifier = Modifier.height(16.dp))

                // Botón para eliminar contacto
                TextButton(
                    onClick = {
                        // Lógica para eliminar contacto
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Eliminar Contacto",
                        color = Color.Red
                    )
                }

                // Botón para bloquear
                TextButton(
                    onClick = {
                        // Lógica para bloquear
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Bloquear",
                        color = Color.Red
                    )
                }
            }
        }
    }
}


// --- Home Page ---
/**
 * Composable que muestra la pantalla de inicio.
 *
 * @param navController controlador de navegación.
 */
@Composable
@Preview
fun muestraHomePage(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "inicio", // Se usa la clave "inicio" en lugar del literal "Inicio"
                    navController = null,
                    showBackButton = false,
                    muestraEngranaje = false,
                    irParaAtras = false
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("ir_a_login"))
                        }
                        Button(
                            onClick = { navController.navigate("registro") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("ir_a_registro"))
                        }
                        Button(
                            onClick = { navController.navigate("contactos") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("contactos"))
                        }
                        Button(
                            onClick = { navController.navigate("ajustes") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("ajustes"))
                        }
                        Button(
                            onClick = { navController.navigate("usuarios") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir("usuarios"))
                        }
                    }
                }
            }
        }
    }
}

// --- SpashScreen ---
/**
 * Composable que muestra la pantalla de carga (Splash Screen).
 *
 * @param navController controlador de navegación.
 */
@Composable
fun SplashScreen(navController: NavHostController) {
    // Efecto para esperar 2 segundos y navegar a "home"
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    MaterialTheme {
        Scaffold {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Muestra el ícono de la app en el centro
                Image(
                    painter = painterResource(
                        Res.drawable.connexus
                    ),
                    contentDescription = "Ícono de la aplicación"
                )
            }
        }
    }
}

//--------------------------------------------------

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
@Composable
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
        topBar = {
            DefaultTopBar(
                title = traducir(titleKey),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = true,
                irParaAtras = true
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.connexus),
                        contentDescription = "Ícono de la aplicación",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(traducir(mensajeKey), style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de código si es necesario
                    if (mostrarCampoCodigo) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text(traducir("codigo_verificacion")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Botones en una Row (alineados horizontalmente)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(rutaBotonPrincipal) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(traducir(textoBotonPrincipalKey))
                        }

                        if (textoBotonSecundarioKey != null && rutaBotonSecundario != null) {
                            Button(
                                onClick = { navController.navigate(rutaBotonSecundario) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir(textoBotonSecundarioKey))
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

// Pantalla de Restablecer Contraseña
/**
 * Composable que muestra la pantalla para restablecer la contraseña.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun PantallaRestablecer(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Realiza la traducción fuera del bloque onClick
    val errorCorreoVacio = traducir("error_correo_vacio")

    MaterialTheme {
        Scaffold(
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
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .padding(padding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(traducir("email")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    errorMessage = if (email.isNotBlank()) {
                                        ""
                                    } else {
                                        errorCorreoVacio
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("enviar_correo"))
                            }
                            Button(
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
                            Button(
                                onClick = { navController.navigate("emailEnSistema") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("degug_restablecer_ok"))
                            }
                            Button(
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
                                color = MaterialTheme.colors.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

// Pantalla de de restablecer contraseña ingresando la nueva contraseña
/**
 * Composable que muestra la pantalla para restablecer la contraseña ingresando una nueva.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun muestraRestablecimientoContasenna(navController: NavHostController) {
    var contrasenna by remember { mutableStateOf("") }
    var confirmarContrasenna by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Realiza la traducción fuera del bloque onClick
    val errorContrasenas = traducir("error_contrasenas")

    MaterialTheme {
        Scaffold(
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
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .padding(padding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = contrasenna,
                            onValueChange = { contrasenna = it },
                            label = { Text(traducir("contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmarContrasenna,
                            onValueChange = { confirmarContrasenna = it },
                            label = { Text(traducir("confirmar_contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
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
                            Button(
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
                                color = MaterialTheme.colors.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable que muestra la pantalla de registro de usuario.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun PantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val errorContrasenas = traducir("error_contrasenas")
    val errorEmailYaRegistrado =
        traducir("error_email_ya_registrado") // Falta implementar y mete en los mapas de idiomas

    // Instanciamos un usuario vacío que se completará si el registro es correcto
    val usuario = Usuario()

    // Usamos un scope para lanzar corrutinas
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("registro"),
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = true,
                    irParaAtras = true
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text(traducir("nombre")) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(traducir("email")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(traducir("contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text(traducir("confirmar_contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                                    // Si no hay error, proceder a completar el usuario y enviarlo a Firestore
                                    if (errorMessage.isEmpty()) {
                                        // Seteamos los valores en el usuario
                                        usuario.setNombreCompleto(nombre)
                                        usuario.setCorreo(email)
                                        usuario.setContrasennia(password)
                                        usuario.setAliasPrivado("Privado_$nombre")
                                        usuario.setAlias(UtilidadesUsuario().generarAliasPublico())
                                        usuario.setDescripcion("Descripción de $nombre")
                                        usuario.setImagenPerfil(UtilidadesUsuario().generarImagenPerfilRandom())

                                        // Agregamos el usuario a la lista local (por ejemplo, UsuariosPreCreados)
                                        UsuariosPreCreados.add(usuario)

                                        // Ejecutamos la función suspend dentro de una corrutina
                                        scope.launch {
                                            FirestoreUsuariosNuestros().addUsuario(usuario)
                                            // Navegamos a la pantalla de login después de agregar el usuario
                                            navController.navigate("login") {
                                                popUpTo("registro") { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("registrar"))
                            }
                            Button(
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
                                color = MaterialTheme.colors.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable que muestra la pantalla de inicio de sesión.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun PantallaLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Mensajes de error (debes definir estas claves en tu mapa de idiomas)
    val errorEmailNingunUsuario =
        traducir("error_email_ningun_usuario") // Ejemplo: "No hay ningún usuario registrado con ese email"
    val errorContrasenaIncorrecta =
        traducir("error_contrasena_incorrecta") // Ejemplo: "Contraseña incorrecta"
    val porFavorCompleta =
        traducir("por_favor_completa") // Ejemplo: "Por favor, completa todos los campos"

    // Scope para lanzar corrutinas
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("iniciar_sesion"),
                    navController = navController,
                    showBackButton = false,
                    irParaAtras = false,
                    muestraEngranaje = false
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(traducir("email")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(traducir("contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("restablecer") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("olvidaste_contrasena"))
                            }
                            Button(
                                onClick = { navController.navigate("registro") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("registrarse"))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    // Validamos que se hayan introducido ambos campos
                                    if (email.isBlank() || password.isBlank()) {
                                        errorMessage = porFavorCompleta
                                        return@launch
                                    }
                                    // Obtén el usuario desde Firestore a través del repositorio
                                    val usuario =
                                        FirestoreUsuariosNuestros().getUsuarioPorCorreo(email)
                                            .firstOrNull()
                                    if (usuario == null) {
                                        // No se encontró ningún usuario con ese email
                                        errorMessage = errorEmailNingunUsuario
                                    } else {
                                        // Se encontró el usuario; comprobamos la contraseña
                                        if (usuario.getContrasennia() != password) {
                                            errorMessage = errorContrasenaIncorrecta
                                        } else {
                                            UsuarioPrincipal =
                                                usuario // Asignamos el usuario encontrado a la variable global
                                            errorMessage = ""
                                            // Login exitoso; navega a "contactos"
                                            navController.navigate("contactos") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("acceder"))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { navController.navigate("zonaPruebas") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Debug: Ir a la zona de pruebas"
                                )
                            }
                        }
                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                errorMessage,
                                color = MaterialTheme.colors.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaZonaPruebas(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Zona de Pruebas",
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = true,
                    irParaAtras = true
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Zona de Pruebas")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("contactos") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("debug_ir_a_contactos"))
                            }
                            Button(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("debug_ajustes_control_cuentas"))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("pruebasObjetosFIrebase") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ir a las pruebas con Firebase")
                            }
                            Button(
                                onClick = { navController.navigate("pruebasSupabase") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ir a las pruebas con Supabase")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("pruebasEncriptacion") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Debug: Ir a las pruebas de encriptación")
                        }
                    }
                }
            }
        }
    }
}