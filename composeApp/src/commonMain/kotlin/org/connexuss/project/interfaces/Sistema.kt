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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.obtenerClaveDesdeImagen
import org.connexuss.project.misc.obtenerImagenAleatoria
import org.connexuss.project.misc.obtenerImagenDesdeId
import org.connexuss.project.misc.sesionActualUsuario
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.clearSession
import org.connexuss.project.persistencia.getRestoredSessionFlow
import org.connexuss.project.persistencia.getSessionFlow
import org.connexuss.project.persistencia.saveSession
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UsuarioBloqueado
import org.connexuss.project.usuario.UsuarioContacto
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
                                    navController.navigate("mostrarPerfil/${usuario.getIdUnicoMio()}")
                                },
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Text(
                                    text = "${traducir("alias_label")} ${usuario.getAliasMio()}",
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = "${traducir("alias_privado_label")} ${usuario.getAliasPrivadoMio()}",
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
                                                text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                                                style = MaterialTheme.typography.subtitle1
                                            )
                                            Text(
                                                text = "${traducir("alias_label")} ${usuario.getAliasMio()}",
                                                style = MaterialTheme.typography.body1
                                            )
                                            Text(
                                                text = "${traducir("activo_label")} ${usuario.getActivoMio()}",
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
fun ChatCard(
    conversacion: Conversacion,
    navController: NavHostController,
    participantes: List<Usuario>,
    ultimoMensaje: Mensaje?,
    bloqueados: Set<String> = emptySet()
) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio()
    val esGrupo = !conversacion.nombre.isNullOrBlank()

    println("👥 Participantes en la conversación ${conversacion.id}:")
    participantes.forEach {
        println(" - ${it.getNombreCompletoMio()} (id: ${it.getIdUnicoMio()})")
    }
    println("🧍 Usuario actual: $currentUserId")

    val otroUsuario = participantes.firstOrNull { it.getIdUnicoMio() != currentUserId }
    val estaBloqueado = otroUsuario?.getIdUnicoMio() in bloqueados

    val displayName = if (esGrupo) {
        conversacion.nombre!!
    } else {
        otroUsuario?.getNombreCompletoMio() ?: conversacion.id
    }

    val nombresParticipantes = if (esGrupo) {
        participantes.joinToString(", ") {
            if (it.getIdUnicoMio() == currentUserId) "Tú" else it.getNombreCompletoMio()
        }
    } else null

    val destino = if (esGrupo) {
        "mostrarChatGrupo/${conversacion.id}"
    } else {
        "mostrarChat/${conversacion.id}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(
                if (!estaBloqueado) Modifier.clickable {
                    println("🧭 Navegando a: $destino")
                    navController.navigate(destino)
                } else Modifier
            ),
        elevation = 4.dp,
        backgroundColor = if (estaBloqueado) Color.Red.copy(alpha = 0.2f) else MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.h6,
                color = if (estaBloqueado) Color.Red else MaterialTheme.colors.onSurface
            )

            nombresParticipantes?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    color = if (estaBloqueado) Color.Red else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            if (ultimoMensaje != null && !estaBloqueado) {
                Text(
                    text = ultimoMensaje.content,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}



// --- Chats PorDefecto ---
@Composable
fun muestraChats(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return

    val repo = remember { SupabaseRepositorioGenerico() }

    var relacionesConversaciones by remember { mutableStateOf<List<ConversacionesUsuario>>(emptyList()) }
    var listaConversaciones by remember { mutableStateOf<List<Conversacion>>(emptyList()) }
    var todosLosUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var mensajes by remember { mutableStateOf<List<Mensaje>>(emptyList()) }
    var usuariosBloqueados by remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(Unit) {
        repo.getAll<ConversacionesUsuario>("conversaciones_usuario").collect {
            relacionesConversaciones = it
        }
    }

    // Convers activas
    LaunchedEffect(relacionesConversaciones) {
        val idsDelUsuario = relacionesConversaciones
            .filter { it.idusuario == currentUserId }
            .map { it.idconversacion }

        repo.getAll<Conversacion>("conversacion").collect { todas ->
            listaConversaciones = todas.filter { it.id in idsDelUsuario }
        }
    }

    // usuarios
    LaunchedEffect(Unit) {
        repo.getAll<Usuario>("usuario").collect {
            todosLosUsuarios = it
        }
    }

    // Mensajes
    LaunchedEffect(Unit) {
        repo.getAll<Mensaje>("mensaje").collect {
            mensajes = it
        }
    }

    //  Usuarios bloqueados
    LaunchedEffect(Unit) {
        repo.getAll<UsuarioBloqueado>("usuario_bloqueados").collect { lista ->
            usuariosBloqueados = lista
                .filter { it.idUsuario == currentUserId }
                .map { it.idBloqueado }
                .toSet()
        }
    }

    val chatsConDatos = listaConversaciones.map { conversacion ->
        val participantes = relacionesConversaciones
            .filter { it.idconversacion == conversacion.id }
            .mapNotNull { relacion ->
                todosLosUsuarios.find { it.getIdUnicoMio() == relacion.idusuario }
            }

        val ultimoMensaje = mensajes
            .filter { it.idconversacion == conversacion.id }
            .maxByOrNull { it.fechaMensaje }

        Triple(conversacion, participantes, ultimoMensaje)
    }

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
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(chatsConDatos) { (conversacion, participantes, ultimoMensaje) ->
                            ChatCard(
                                conversacion = conversacion,
                                navController = navController,
                                participantes = participantes,
                                ultimoMensaje = ultimoMensaje,
                                bloqueados = usuariosBloqueados
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("nuevo") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
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
fun muestraContactos(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.idUnico ?: return
    println("🪪 ID usuario actual: $currentUserId")
    val repo = remember { SupabaseRepositorioGenerico() }
    val scope = rememberCoroutineScope()

    var registrosContacto by remember { mutableStateOf<List<UsuarioContacto>>(emptyList()) }
    var contactos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var usuariosBloqueados by remember { mutableStateOf<Set<String>>(emptySet()) }


    var showNuevoContactoDialog by remember { mutableStateOf(false) }
    var nuevoContactoId by remember { mutableStateOf("") }

    var showNuevoChatDialog by remember { mutableStateOf(false) }
    var contactosSeleccionados by remember { mutableStateOf<Set<String>>(emptySet()) }
    var nombreGrupo by remember { mutableStateOf("") }

    LaunchedEffect(currentUserId) {
        repo.getAll<UsuarioContacto>("usuario_contacto").collect { lista ->
            registrosContacto = lista.filter { it.idUsuario == currentUserId }
        }
    }

    LaunchedEffect(registrosContacto) {
        val idsDeContactos = registrosContacto.map { it.idContacto }
        if (idsDeContactos.isNotEmpty()) {
            repo.getAll<Usuario>("usuario").collect { usuarios ->
                contactos = usuarios.filter { it.idUnico in idsDeContactos }
            }
        } else {
            contactos = emptyList()
        }
    }

    LaunchedEffect(currentUserId) {
        repo.getAll<UsuarioBloqueado>("usuario_bloqueados").collect { lista ->
            usuariosBloqueados = lista
                .filter { it.idUsuario == currentUserId }
                .map { it.idBloqueado }
                .toSet()
        }
    }


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
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(contactos) { usuario ->
                            val estaBloqueado = usuario.idUnico in usuariosBloqueados

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .then(
                                        if (!estaBloqueado)
                                            Modifier.clickable {
                                                navController.navigate("mostrarPerfilUsuario/${usuario.idUnico}")
                                            }
                                        else Modifier // no clickable
                                    ),
                                elevation = 4.dp,
                                backgroundColor = if (estaBloqueado) Color.Red.copy(alpha = 0.2f) else Color.White
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${traducir("nombre_label")}: ${usuario.getNombreCompletoMio()}",
                                        style = MaterialTheme.typography.subtitle1,
                                        color = if (estaBloqueado) Color.Red else Color.Unspecified
                                    )
                                    Text(
                                        text = "${traducir("alias_label")}: ${usuario.getAliasMio()}",
                                        style = MaterialTheme.typography.body1,
                                        color = if (estaBloqueado) Color.Red else Color.Unspecified
                                    )
                                    if (estaBloqueado) {
                                        Text(
                                            text = "Bloqueado",
                                            style = MaterialTheme.typography.caption,
                                            color = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Button(
                            onClick = { showNuevoContactoDialog = true },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(text = traducir("nuevo_contacto"))
                        }
                        Button(
                            onClick = { showNuevoChatDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Nuevo chat")
                        }
                    }
                }

                if (showNuevoContactoDialog) {
                    AlertDialog(
                        onDismissRequest = { showNuevoContactoDialog = false },
                        title = { Text(text = traducir("nuevo_contacto")) },
                        text = {
                            Column {
                                Text(text = "Introduce el alias privado del usuario:")
                                OutlinedTextField(
                                    value = nuevoContactoId,
                                    onValueChange = { nuevoContactoId = it },
                                    label = { Text(text = "Alias Privado") }
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val aliasBuscado = nuevoContactoId.trim()
                                    println("🔍 Buscando usuario con alias privado: $aliasBuscado")

                                    scope.launch {
                                        try {
                                            val todosUsuarios = repo.getAll<Usuario>("usuario").first()
                                            val usuarioEncontrado = todosUsuarios.find { it.getAliasPrivadoMio() == aliasBuscado }

                                            if (usuarioEncontrado != null) {
                                                val idEncontrado = usuarioEncontrado.idUnico
                                                if (idEncontrado == currentUserId) {
                                                    println("⚠️ No puedes agregarte a ti mismo como contacto")
                                                    return@launch
                                                }

                                                val nuevoRegistro1 = UsuarioContacto(
                                                    idUsuario = currentUserId,
                                                    idContacto = idEncontrado
                                                )
                                                val nuevoRegistro2 = UsuarioContacto(
                                                    idUsuario = idEncontrado,
                                                    idContacto = currentUserId
                                                )

                                                println("📤 Insertando registros mutuos...")
                                                repo.addItem("usuario_contacto", nuevoRegistro1)
                                                repo.addItem("usuario_contacto", nuevoRegistro2)
                                                println("✅ Contactos agregados")

                                                repo.getAll<UsuarioContacto>("usuario_contacto").collect { lista ->
                                                    registrosContacto = lista.filter { it.idUsuario == currentUserId }
                                                }
                                            } else {
                                                println("❌ No se encontró usuario con ese alias privado")
                                            }

                                        } catch (e: Exception) {
                                            println("❌ Error buscando o agregando contacto: ${e.message}")
                                        }

                                        nuevoContactoId = ""
                                        showNuevoContactoDialog = false
                                    }
                                }
                            ) {
                                Text(text = traducir("guardar"))
                            }
                        }
                        ,
                        dismissButton = {
                            TextButton(onClick = {
                                nuevoContactoId = ""
                                showNuevoContactoDialog = false
                            }) {
                                Text(text = traducir("cancelar"))
                            }
                        }
                    )
                }

                if (showNuevoChatDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNuevoChatDialog = false
                            contactosSeleccionados = emptySet()
                            nombreGrupo = ""
                        },
                        title = { Text(text = "Crear nuevo chat") },
                        text = {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text("Selecciona participantes:")
                                contactos.forEach { usuario ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = contactosSeleccionados.contains(usuario.idUnico),
                                            onCheckedChange = {
                                                contactosSeleccionados = if (it)
                                                    contactosSeleccionados + usuario.idUnico
                                                else
                                                    contactosSeleccionados - usuario.idUnico
                                            }
                                        )
                                        Text(text = usuario.getNombreCompletoMio())
                                    }
                                }
                                if (contactosSeleccionados.size > 1) {
                                    Spacer(Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = nombreGrupo,
                                        onValueChange = { nombreGrupo = it },
                                        label = { Text("Nombre del grupo") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                scope.launch {
                                    try {
                                        val participantes = contactosSeleccionados + currentUserId
                                        val nuevaConversacion = Conversacion(
                                            nombre = if (contactosSeleccionados.size > 1) nombreGrupo else null
                                        )
                                        repo.addItem("conversacion", nuevaConversacion)

                                        participantes.forEach { idUsuario ->
                                            val relacion = ConversacionesUsuario(
                                                idusuario = idUsuario,
                                                idconversacion = nuevaConversacion.id
                                            )
                                            repo.addItem("conversaciones_usuario", relacion)
                                        }

                                        showNuevoChatDialog = false
                                        contactosSeleccionados = emptySet()
                                        nombreGrupo = ""

                                    } catch (e: Exception) {
                                        println("❌ Error creando nuevo chat: ${e.message}")
                                    }
                                }
                            }) { Text("Crear") }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showNuevoChatDialog = false
                                contactosSeleccionados = emptySet()
                                nombreGrupo = ""
                            }) {
                                Text("Cancelar")
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
            usuario.getImagenPerfilMio()?.let { painterResource(it as DrawableResource) }?.let {
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
                    text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "${traducir("alias_publico")} ${usuario.getAliasMio()}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${traducir("alias_privado")} ${usuario.getAliasPrivadoMio()}",
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
fun muestraAjustes(navController: NavHostController = rememberNavController(), settingsState: SettingsState) {
    val user = UsuarioPrincipal
    val scope = rememberCoroutineScope()
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
                            onClick = { navController.navigate("ajustesControlCuentas") },
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
                            onClick = {
                                // Cerrar sesión
                                scope.launch {
                                    try {
                                        // 1) Cerrar sesión en Supabase (limpia el cliente)
                                        Supabase.client.auth.signOut()

                                        // 2) Limpiar almacenamiento local de tokens y usuario
                                        settingsState.clearSession()

                                        // 3) Reiniciar variables globales
                                        sesionActualUsuario = null
                                        UsuarioPrincipal   = null

                                        println("🔒 Sesión local y remota cerrada")
                                    } catch (e: Exception) {
                                        println("❌ Error cerrando sesión: ${e.message}")
                                    }
                                }
                                // Navegar a la pantalla de inicio de sesión
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            },
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
val repo = SupabaseUsuariosRepositorio()

@Composable
fun mostrarPerfil(navController: NavHostController, usuarioU: Usuario?) {
    // Se recibe el usuario
    //val usuario = usuarioU

    //variable para actualizar datos
    val coroutineScope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(Unit) {
        try {
            val repo = SupabaseUsuariosRepositorio()
            val uid = Supabase.client.auth.currentUserOrNull()?.id
            println("🪪 UID actual autenticado: $uid")

            usuario = repo.getUsuarioAutenticado()
            val usuarioEncontrado = repo.getUsuarioAutenticado()
            println("🧠 Usuario encontrado en la tabla: $usuarioEncontrado")

        } catch (e: Exception) {
            println("Error cargando usuario autenticado: ${e.message}")

        }
    }



    // Dialogs
    var showDialogNombre by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }

    // Campos del usuario
    var aliasPrivado by remember { mutableStateOf("") }
    var aliasPublico by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var contrasennia by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isNameVisible by remember { mutableStateOf(false) }

    //Actualiza los campos del usuario al cargar la pantalla
    LaunchedEffect(usuario) {
        usuario?.let {
            aliasPrivado = it.getAliasPrivadoMio()
            aliasPublico = it.getAliasMio()
            descripcion = it.getDescripcionMio()
            contrasennia = it.getContrasenniaMio()
            email = it.getCorreoMio()
        }
    }

    val imagenPerfilState = remember(usuario) {
        mutableStateOf(obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio()))
    }

    LaunchedEffect(usuario?.getImagenPerfilIdMio()) {
        imagenPerfilState.value = obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio())
    }


    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "perfil",
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
                                Image(
                                    painter = painterResource(imagenPerfilState.value ?: Res.drawable.avatar),
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
                                    val nuevaImagen = obtenerImagenAleatoria()
                                    imagenPerfilState.value = nuevaImagen
                                    usuario?.apply {
                                        setImagenPerfilMia(nuevaImagen)
                                        setImagenPerfilIdMia(obtenerClaveDesdeImagen(nuevaImagen))
                                    }
                                }
                                ,
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
                            }
                            // Botones inferiores
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        usuario?.apply {
                                            setAliasPrivadoMio(aliasPrivado)
                                            setAliasMio(aliasPublico)
                                            setDescripcionMio(descripcion)
                                            setContrasenniaMio(contrasennia)
                                            setCorreoMio(email)
                                        }

                                        usuario?.let {
                                            coroutineScope.launch {
                                                try {
                                                    // Solo actualiza si es distinto
                                                    val authUser = Supabase.client.auth.currentUserOrNull()
                                                    if (contrasennia != it.getContrasenniaMio()) {
                                                        Supabase.client.auth.updateUser {
                                                            password = contrasennia
                                                        }
                                                    }

                                                    // Luego actualiza en la tabla usuario
                                                    repo.updateUsuario(it)

                                                    val usuarioActualizado = repo.getUsuarioAutenticado()
                                                    usuario = usuarioActualizado

                                                    navController.popBackStack()

                                                } catch (e: Exception) {
                                                    println("Error al actualizar usuario: ${e.message}")
                                                }
                                            }
                                        }
                                    }
                                )
                                {
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
                        usuario?.setContrasenniaMio(nuevoNombre)
                        showDialogNombre = false

                        usuario?.let {
                            coroutineScope.launch {
                                try {
                                    // Cambiar contraseña en Auth
                                    Supabase.client.auth.updateUser {
                                        password = nuevoNombre
                                    }

                                    // Luego actualizar en la tabla usuario
                                    repo.updateUsuario(it)
                                } catch (e: Exception) {
                                    println("❌ Error al cambiar contraseña: ${e.message}")
                                }
                            }
                        }
                    }
                )
                {
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

    val scope = rememberCoroutineScope()
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(userId) {
        if (userId == null) return@LaunchedEffect

        val todosUsuarios = repo.getAll<Usuario>("usuario").first()
        usuario = todosUsuarios.find { it.getIdUnicoMio() == userId }

        println("🙋 Usuario cargado: ${usuario?.getNombreCompletoMio()}")
    }
    if (usuario == null) return


    Scaffold(
        topBar = {
            DefaultTopBar(
                title = usuario?.getNombreCompletoMio() ?: "Perfil",
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
            var aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivadoMio() ?: "") }
            var aliasPublico by remember { mutableStateOf(usuario?.getAliasMio() ?: "") }
            var descripcion by remember { mutableStateOf(usuario?.getDescripcionMio() ?: "") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imagenDrawable = remember(usuario?.getImagenPerfilIdMio()) {
                    obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio())
                }

                Image(
                    painter = painterResource(imagenDrawable),
                    contentDescription = "Imagen de perfil de ${usuario?.getNombreCompletoMio()}",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                )


                OutlinedTextField(
                    value = aliasPrivado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Alias Privado") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = aliasPublico,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Alias Público") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        if (usuario != null) {
                            scope.launch {
                                val repo = SupabaseRepositorioGenerico()

                                try {
                                    val relaciones = repo.getAll<ConversacionesUsuario>("conversaciones_usuario").first()

                                    // IDs de conversaciones donde estén ambos
                                    val conversacionesComunes = relaciones
                                        .groupBy { it.idconversacion }
                                        .filter { (_, users) ->
                                            val ids = users.map { it.idusuario }
                                            currentUserId in ids && usuario!!.getIdUnicoMio() in ids && ids.size == 2
                                        }
                                        .keys

                                    println("🔍 Conversaciones individuales encontradas: $conversacionesComunes")

                                    for (convId in conversacionesComunes) {
                                        // Eliminar relaciones en conversaciones_usuario
                                        repo.deleteItem<ConversacionesUsuario>(
                                            tableName = "conversaciones_usuario",
                                            idField = "idconversacion",
                                            idValue = convId
                                        )

                                        // Eliminar conversación
                                        repo.deleteItem<Conversacion>(
                                            tableName = "conversacion",
                                            idField = "id",
                                            idValue = convId
                                        )

                                        println("🗑️ Eliminada conversación $convId")
                                    }

                                    //Eliminar también contacto en tabla contactos
                                    repo.deleteItemMulti<UsuarioContacto>(
                                        tableName = "usuario_contacto",
                                        conditions = mapOf(
                                            "idusuario" to currentUserId,
                                            "idcontacto" to usuario!!.getIdUnicoMio()
                                        )
                                    )

                                    repo.deleteItemMulti<UsuarioContacto>(
                                        tableName = "usuario_contacto",
                                        conditions = mapOf(
                                            "idusuario" to usuario!!.getIdUnicoMio(),
                                            "idcontacto" to currentUserId
                                        )
                                    )

                                    usuario = null
                                    navController.popBackStack()
                                    return@launch


                                } catch (e: Exception) {
                                    println("❌ Error eliminando contacto: ${e.message}")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Eliminar Contacto",
                        color = Color.Red
                    )
                }


                TextButton(
                    onClick = {
                        if (usuario != null) {
                            scope.launch {
                                try {
                                    val nuevoBloqueo = UsuarioBloqueado(
                                        idUsuario = currentUserId,
                                        idBloqueado = usuario!!.getIdUnicoMio()
                                    )
                                    repo.addItem("usuario_bloqueados", nuevoBloqueo)
                                    println("🚫 Usuario bloqueado correctamente")
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    println("❌ Error al bloquear usuario: ${e.message}")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Bloquear", color = Color.Red)
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
fun SplashScreen(navController: NavHostController, settingsState: SettingsState) {

    // 1) Convertimos el Flow<UserSession?> en State
//    val session by settingsState
//        .getSessionFlow()
//        .collectAsState(initial = null)

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
            UsuarioPrincipal   = usuario

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
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("restablecer_contrasena"),
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = false,
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
                        modifier = modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = "Logo",
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    if (email.isBlank()) {
                                        error = "Introduce tu correo"
                                        return@launch
                                    }

                                    try {
                                        Supabase.client.auth.resetPasswordForEmail(email)
                                        mensaje = "📧 Se ha enviado un correo para restablecer tu contraseña. Ábrelo desde tu navegador y sigue los pasos."
                                        error = ""
                                    } catch (e: Exception) {
                                        error = "❌ Error al enviar el correo: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enviar correo de restablecimiento")
                        }

                        Spacer(Modifier.height(16.dp))

                        if (mensaje.isNotEmpty()) {
                            Text(mensaje, color = Color.Green, textAlign = TextAlign.Center)
                        }

                        if (error.isNotEmpty()) {
                            Text(error, color = MaterialTheme.colors.error, textAlign = TextAlign.Center)
                        }

                        Spacer(Modifier.height(24.dp))

                        Text(
                            "Una vez restablezcas tu contraseña desde el navegador, vuelve a esta app y entra con tu nueva clave.",
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver al login")
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
    var mensaje by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val repo = remember { SupabaseUsuariosRepositorio() }

    val errorContrasenas = traducir("error_contrasenas")

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("restablecer_contrasena"),
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = false,
                    irParaAtras = false
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
                        modifier = modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = contrasenna,
                            onValueChange = { contrasenna = it },
                            label = { Text(traducir("contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = confirmarContrasenna,
                            onValueChange = { confirmarContrasenna = it },
                            label = { Text(traducir("confirmar_contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (contrasenna != confirmarContrasenna || contrasenna.isBlank()) {
                                    errorMessage = errorContrasenas
                                    return@Button
                                }

                                scope.launch {
                                    try {
                                        val user = Supabase.client.auth.currentUserOrNull()
                                        if (user == null) {
                                            errorMessage = "⚠️ No hay sesión activa para actualizar."
                                            return@launch
                                        }

                                        // 1. Actualizar en Auth
                                        Supabase.client.auth.updateUser {
                                            password = contrasenna
                                        }

                                        // 2. Actualizar también en la tabla usuario
                                        repo.updateCampo(
                                            tabla = "usuario",
                                            campo = "contrasennia",
                                            valor = contrasenna,
                                            idCampo = "idunico",
                                            idValor = user.id
                                        )

                                        mensaje = "✅ Contraseña restablecida con éxito."
                                        errorMessage = ""
                                        navController.navigate("login") {
                                            popUpTo("restablecerNueva") { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "❌ Error: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("restablecer"))
                        }

                        if (mensaje.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(mensaje, color = Color.Green)
                        }

                        if (errorMessage.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(errorMessage, color = MaterialTheme.colors.error)
                        }
                    }
                }
            }
        }
    }
}




//metodo que comprueba correo
fun esEmailValido(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    return regex.matches(email)
}


/**
 * Composable que muestra la pantalla de registro de usuario.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun PantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var emailInterno by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val repoSupabase = SupabaseUsuariosRepositorio()

    val errorContrasenas = traducir("error_contrasenas")
    val errorEmailYaRegistrado =
        traducir("error_email_ya_registrado") // Falta implementar y mete en los mapas de idiomas

    val usuario = Usuario()

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
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
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
                            value = emailInterno,
                            onValueChange = { emailInterno = it },
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

                                    if (errorMessage.isEmpty()) {
                                        scope.launch {
                                            try {
                                                val emailTrimmed = emailInterno.trim()

                                                if (!esEmailValido(emailTrimmed)) {
                                                    errorMessage = "Formato de correo inválido"
                                                    return@launch
                                                }

                                                // Registro en Supabase Auth
                                                val authResult = Supabase.client.auth.signUpWith(Email) {
                                                    this.email = emailTrimmed
                                                    this.password = password
                                                }
                                                navController.navigate("registroVerificaCorreo/${emailTrimmed}/${nombre}/${password}")


                                            } catch (e: Exception) {
                                                errorMessage = "❌ Error al registrar: ${e.message}"
                                                //navController.navigate("login")
                                            }
                                        }
                                    }
                                }


                                ,
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
 * Composable que muestra la pantalla de verificación de correo.
 *
 * @param navController controlador de navegación.
 */

@Composable
fun PantallaVerificaCorreo(
    navController: NavHostController,
    email: String?,
    nombre: String?,
    password: String?
) {
    val scope = rememberCoroutineScope()
    val repo = remember { SupabaseUsuariosRepositorio() }

    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Verificación de correo",
                navController = navController,
                showBackButton = false,
                muestraEngranaje = false,
                irParaAtras = false
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Hemos enviado un correo de verificación a:")
                Text(email ?: "", style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(16.dp))
                Text("Verifica tu cuenta, y luego pulsa el botón para continuar.")
                Spacer(Modifier.height(24.dp))

                Button(onClick = {
                    scope.launch {
                        try {
                            // 🛡Reautenticación
                            if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                                Supabase.client.auth.signInWith(Email) {
                                    this.email = email
                                    this.password = password
                                }
                            }

                            val usuarioActual = Supabase.client.auth.currentUserOrNull()

                            if (usuarioActual?.emailConfirmedAt != null) {
                                val imagenAleatoria = UtilidadesUsuario().generarImagenPerfilAleatoria()

                                val nuevoUsuario = Usuario(
                                    idUnico = usuarioActual.id,
                                    correo = email ?: "",
                                    nombre = nombre ?: "",
                                    aliasPrivado = "Privado_$nombre",
                                    aliasPublico = UtilidadesUsuario().generarAliasPublico(),
                                    activo = true,
                                    descripcion = "Perfil creado automáticamente",
                                    contrasennia = password ?: "",
                                    imagenPerfilId = imagenAleatoria.id
                                ).apply {
                                    imagenPerfil = imagenAleatoria.resource
                                }



                                repo.addUsuario(nuevoUsuario)

                                navController.navigate("login") {
                                    popUpTo("registroVerificaCorreo") { inclusive = true }
                                }
                            } else {
                                mensaje = "❗ Tu correo aún no está verificado."
                            }
                        } catch (e: Exception) {
                            //mensaje = "❌ Error: ${e.message}"
                            navController.navigate("login")
                        }
                    }
                }) {
                    Text("Ya lo he verificado")
                }

                Spacer(Modifier.height(12.dp))

                Button(onClick = {
                    scope.launch {
                        try {
                            if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                                Supabase.client.auth.signUpWith(Email) {
                                    this.email = email
                                    this.password = password
                                }
                                mensaje = "📧 Correo reenviado correctamente."
                            } else {
                                mensaje = "⚠️ Falta información para reenviar el correo."
                            }
                        } catch (e: Exception) {
                            mensaje = "❌ Error al reenviar: ${e.message}"
                        }
                    }
                }) {
                    Text("Reenviar correo")
                }

                if (mensaje.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(mensaje, color = MaterialTheme.colors.error)
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
fun PantallaLogin(navController: NavHostController, settingsState: SettingsState) {
    var emailInterno by remember { mutableStateOf("") }
    var passwordInterno by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Estados para el botón Debug oculto
    var showDebug by remember { mutableStateOf(false) }
    var logoTapCount by remember { mutableStateOf(0) }
    val tapsToReveal = 5  // Número de toques necesarios para mostrar Debug

    val repoSupabase = SupabaseUsuariosRepositorio()

    val errorEmailNingunUsuario = traducir("error_email_ningun_usuario")
    val errorContrasenaIncorrecta = traducir("error_contrasena_incorrecta")
    val porFavorCompleta = traducir("por_favor_completa")

    var rememberMe by rememberSaveable { mutableStateOf(false) }

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
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .padding(padding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo con área clickable oculta para revelar Debug
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    logoTapCount++
                                    if (logoTapCount >= tapsToReveal) {
                                        showDebug = true
                                    }
                                }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = emailInterno,
                            onValueChange = { emailInterno = it },
                            label = { Text(traducir("email")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = passwordInterno,
                            onValueChange = { passwordInterno = it },
                            label = { Text(traducir("contrasena")) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = traducir("recuerdame"),
                                style = MaterialTheme.typography.body1
                            )
                        }
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
                                    if (emailInterno.isBlank() || passwordInterno.isBlank()) {
                                        errorMessage = porFavorCompleta
                                        return@launch
                                    }

                                    try {
                                        val usuario = repoSupabase.getUsuarioPorEmail(emailInterno.trim()).firstOrNull()

                                        if (usuario == null) {
                                            errorMessage = errorEmailNingunUsuario
                                        } else {
                                            UsuarioPrincipal = usuario
                                            println("Usuario autenticado: $UsuarioPrincipal")

                                            // Iniciar sesión en Supabase
                                            // importante...
                                            /*Supabase.client.auth.signInWith(
                                                provider = Email
                                            ) {
                                                email = UsuarioPrincipal!!.correo
                                                password = UsuarioPrincipal!!.contrasennia
                                            }*/
                                            //utilizar uno de los dos (el de arriba te permite loguearte con el email y la contraseña de supabase. el de abajo con auth.
                                            Supabase.client.auth.signInWith(Email) {
                                                email = emailInterno.trim()
                                                password = passwordInterno.trim()
                                            }

                                            // Actualizar la sesión actual
                                            sesionActualUsuario = Supabase.client.auth.currentSessionOrNull()
                                            errorMessage = ""




                                            // Persistir solo si rememberMe=true
                                            if (rememberMe && sesionActualUsuario != null) {
                                                val userJson = Json.encodeToString(Usuario.serializer(), usuario)
                                                settingsState.saveSession(sesionActualUsuario!!, UsuarioPrincipal!!)
                                            }


                                            navController.navigate("contactos") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("acceder"))
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Debug solo visible tras suficientes toques
                        if (showDebug) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = { navController.navigate("zonaPruebas") },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Debug: Ir a la zona de pruebas")
                                }
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
                                onClick = { navController.navigate("pruebasTextosRealtime") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ir a las pruebas de textos Realtime")
                            }
                            Button(
                                onClick = { navController.navigate("supabasePruebas") },
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("pruebasPersistencia") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Debug: Ir a las pruebas de persistencia de datos")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("zonaReportes") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ADMIN: Ir a la zona de reportes")
                        }
                    }
                }
            }
        }
    }
}