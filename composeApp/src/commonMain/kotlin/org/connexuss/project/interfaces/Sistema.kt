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
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.PostsRepository
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.UsuariosPreCreados
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.supabase.instanciaSupabaseClient
import org.connexuss.project.supabase.supabaseClient
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
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
fun ChatCard(conversacion: Conversacion, navController: NavHostController) {
    // Se define el nombre a mostrar: si 'nombre' no es nulo o vacío se usa; de lo contrario, se usa el 'id'
    val displayName =
        if (!conversacion.nombre.isNullOrBlank()) conversacion.nombre else conversacion.id

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // Al no disponer de propiedades para identificar grupos o participantes,
                // simplemente navegamos a la pantalla de chat individual
                navController.navigate("mostrarChat/${conversacion.id}")
            },
        elevation = 4.dp
    ) {
        // Mostramos únicamente el nombre del chat (o el id, si no hubiera nombre)
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Chat: $displayName")
        }
    }
}

// --- Chats PorDefecto ---
@Composable
fun muestraChats(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return

    val repo = remember { SupabaseRepositorioGenerico() }
    var conversacionesUsuario by remember { mutableStateOf<List<ConversacionesUsuario>>(emptyList()) }
    var listaConversaciones by remember { mutableStateOf<List<Conversacion>>(emptyList()) }

    // Recupera las relaciones (conversaciones_usuario) para el UsuarioPrincipal
    LaunchedEffect(currentUserId) {
        repo.getAll<ConversacionesUsuario>("conversaciones_usuario")
            .collect { convUsers ->
                // Filtramos las relaciones para que pertenezcan al usuario actual
                conversacionesUsuario = convUsers.filter { it.idusuario == currentUserId }
            }
    }

    // Una vez obtenidas las relaciones, extraemos los IDs de conversación y consultamos la tabla conversacion
    LaunchedEffect(conversacionesUsuario) {
        // Lista de IDs de conversación del usuario
        val convIds = conversacionesUsuario.map { it.idconversacion }
        repo.getAll<Conversacion>("conversacion")
            .collect { allConversaciones ->
                // Filtramos las conversaciones cuya id esté en convIds
                listaConversaciones = allConversaciones.filter { it.id in convIds }
            }
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listaConversaciones) { conversacion ->
                            ChatCard(conversacion = conversacion, navController = navController)
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
    // Se obtiene el id del UsuarioPrincipal (no se asume que tenga métodos de lista)
    val currentUserId = UsuarioPrincipal?.idUnico ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    val scope = rememberCoroutineScope()

    // Estado que contendrá los registros de la tabla "usuario_contacto" para el UsuarioPrincipal.
    var registrosContacto by remember { mutableStateOf<List<UsuarioContacto>>(emptyList()) }
    // A partir de esos registros, se filtrarán los usuarios precargados.
    val contactos = UsuariosPreCreados.filter { usuario ->
        registrosContacto.any { it.idContacto == usuario.idUnico }
    }

    // Consulta a la tabla "usuario_contacto" para traer los contactos relacionados al UsuarioPrincipal.
    LaunchedEffect(currentUserId) {
        repo.getAll<UsuarioContacto>("usuario_contacto").collect { lista ->
            // Se filtran solo aquellos registros donde idusuario coincida con el UsuarioPrincipal.
            registrosContacto = lista.filter { it.idUsuario == currentUserId }
        }
    }

    // Estados para el AlertDialog de "Nuevo Contacto"
    var showNuevoContactoDialog by remember { mutableStateOf(false) }
    var nuevoContactoId by remember { mutableStateOf("") }

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
                    // Lista de contactos obtenidos de la consulta.
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(contactos) { usuario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${traducir("nombre_label")}: ${usuario.getNombreCompletoMio()}",
                                        style = MaterialTheme.typography.subtitle1
                                    )
                                    Text(
                                        text = "${traducir("alias_label")}: ${usuario.getAliasMio()}",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                        }
                    }

                    // Botón para agregar un nuevo contacto
                    Button(
                        onClick = { showNuevoContactoDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = traducir("nuevo_contacto"))
                    }
                }

                // AlertDialog para agregar un nuevo contacto
                if (showNuevoContactoDialog) {
                    AlertDialog(
                        onDismissRequest = { showNuevoContactoDialog = false },
                        title = { Text(text = traducir("nuevo_contacto")) },
                        text = {
                            Column {
                                Text(text = "Introduce el idUnico del usuario:")
                                OutlinedTextField(
                                    value = nuevoContactoId,
                                    onValueChange = { nuevoContactoId = it },
                                    label = { Text(text = "idUnico") }
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val idContactoIngresado = nuevoContactoId.trim()
                                    // Se verifica que el id ingresado exista en la lista de usuarios precargados.
                                    val contactoEncontrado = UsuariosPreCreados.find { it.idUnico == idContactoIngresado }
                                    if (contactoEncontrado != null) {
                                        scope.launch {
                                            // Se inserta un nuevo registro en la tabla "usuario_contacto"
                                            val nuevoRegistro = UsuarioContacto(
                                                idUsuario = currentUserId,
                                                idContacto = idContactoIngresado
                                            )
                                            repo.addItem("usuario_contacto", nuevoRegistro)
                                            // Se consulta nuevamente la tabla para actualizar el estado local.
                                            repo.getAll<UsuarioContacto>("usuario_contacto").collect { lista ->
                                                registrosContacto = lista.filter { it.idUsuario == currentUserId }
                                            }
                                        }
                                    }
                                    nuevoContactoId = ""
                                    showNuevoContactoDialog = false
                                }
                            ) { Text(text = traducir("guardar")) }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    nuevoContactoId = ""
                                    showNuevoContactoDialog = false
                                }
                            ) { Text(text = traducir("cancelar")) }
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
    var showDialogEmail by remember { mutableStateOf(false) }
    var nuevoEmail by remember { mutableStateOf("") }

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


    // Estado para la imagen de perfil (para refrescar la UI al cambiarla)
    val imagenPerfilState = remember { mutableStateOf(usuario?.getImagenPerfilMio()) }

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
                                    //val nuevaImagen = usuario.generarImagenPerfilRandom()
                                    //usuario.setImagenPerfilMia(nuevaImagen)
                                    //imagenPerfilState.value = nuevaImagen
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
                                                    println(" Enviando actualización a Supabase...")
                                                    repo.updateUsuario(it)

                                                    // Recarga el usuario actualizado desde Supabase
                                                    val usuarioActualizado = repo.getUsuarioAutenticado()
                                                    usuario = usuarioActualizado

                                                    println("Usuario recargado tras actualización: $usuarioActualizado")

                                                    // Navegación atrás (opcional)
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
                        usuario?.setCorreoMio(nuevoEmail)
                        showDialogEmail = false

                        usuario?.let {
                            coroutineScope.launch {
                                try {
                                    repo.updateUsuario(it)
                                } catch (e: Exception) {
                                    //Log.e("Perfil", "Error actualizando email: ${e.message}")
                                }
                            }
                        }
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
                        usuario?.setContrasenniaMio(nuevoNombre)
                        showDialogNombre = false

                        usuario?.let {
                            coroutineScope.launch {
                                try {
                                    repo.updateUsuario(it)
                                } catch (e: Exception) {
                                    // Manejo de error (no se por que me da error el Log)
                                }
                            }
                        }
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
    val usuario = UsuariosPreCreados.find { it.getIdUnicoMio() == userId }

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
            var aliasPrivado by remember { mutableStateOf(usuario.getAliasPrivadoMio()) }
            var aliasPublico by remember { mutableStateOf(usuario.getAliasMio()) }
            var descripcion by remember { mutableStateOf(usuario.getDescripcionMio()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de perfil
                usuario.getImagenPerfilMio()?.let { painterResource(it) }?.let {
                    Image(
                        painter = it,
                        contentDescription = "Imagen de perfil de ${usuario.getNombreCompletoMio()}",
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val repoSupabase = SupabaseUsuariosRepositorio()

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

                                    if (errorMessage.isEmpty()) {
                                        scope.launch {
                                            try {
                                                val emailTrimmed = email.trim()

                                                if (!esEmailValido(emailTrimmed)) {
                                                    errorMessage = "Formato de correo inválido"
                                                    return@launch
                                                }

                                                // 1. Registro en Supabase Auth
                                                val authResult = Supabase.client.auth.signUpWith(Email) {
                                                    this.email = emailTrimmed
                                                    this.password = password
                                                }

                                                val uid = Supabase.client.auth.currentUserOrNull()?.id
                                                    ?: throw Exception("No se pudo obtener el UID del usuario autenticado")

                                                // 2. Crear objeto Usuario con el mismo ID que auth.uid()
                                                val nuevoUsuario = Usuario(
                                                    idUnico = uid,
                                                    nombre = nombre,
                                                    correo = emailTrimmed,
                                                    aliasPublico = UtilidadesUsuario().generarAliasPublico(),
                                                    aliasPrivado = "Privado_$nombre",
                                                    activo = true,
                                                    descripcion = "Descripción de $nombre",
                                                    contrasennia = password
                                                )

                                                println("Nuevo usuario: $nuevoUsuario")
                                                println("UID Supabase actual: $uid")


                                                // 3. Guardar en tabla usuario
                                                repoSupabase.addUsuario(nuevoUsuario)

                                                // 4. ir al login
                                                navController.navigate("login") {
                                                    popUpTo("registro") { inclusive = true }
                                                }

                                            } catch (e: Exception) {
                                                errorMessage = "Error: ${e.message}"
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
 * Composable que muestra la pantalla de inicio de sesión.
 *
 * @param navController controlador de navegación.
 */
@Composable
fun PantallaLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val repoSupabase = SupabaseUsuariosRepositorio()

    val errorEmailNingunUsuario =
        traducir("error_email_ningun_usuario")
    val errorContrasenaIncorrecta =
        traducir("error_contrasena_incorrecta")
    val porFavorCompleta =
        traducir("por_favor_completa")

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
                                    if (email.isBlank() || password.isBlank()) {
                                        errorMessage = porFavorCompleta
                                        return@launch
                                    }

                                    try {
                                        Supabase.client.auth.signInWith(Email) {
                                            this.email = email.trim()
                                            this.password = password
                                        }

                                        val usuario = repoSupabase.getUsuarioPorEmail(email.trim()).firstOrNull()

                                        if (usuario == null) {
                                            errorMessage = errorEmailNingunUsuario
                                        } else {
                                            UsuarioPrincipal = usuario
                                            errorMessage = ""

                                            navController.navigate("contactos") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }

                                    } catch (e: Exception) {
                                        errorMessage = "Error: ${e.message}"
                                    }
                                }
                            }
                            ,
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
//                            Button(
//                                onClick = { navController.navigate("pruebasObjetosFIrebase") },
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Text("Debug: Ir a las pruebas con Firebase")
//                            }
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
                    }
                }
            }
        }
    }
}