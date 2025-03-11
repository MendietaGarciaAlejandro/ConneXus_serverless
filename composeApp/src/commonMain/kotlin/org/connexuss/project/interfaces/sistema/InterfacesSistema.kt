package org.connexuss.project.interfaces.sistema

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.*
import kotlinx.coroutines.delay

import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.datos.UsuarioPrincipal
import org.connexuss.project.interfaces.modificadorTamannio.LimitaTamanioAncho

import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DefaultTopBar(
    title: String,
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
                Text(text = title)
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
                        contentDescription = "Atrás"
                    )
                }
            }
        } else null,
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = {
                    // Navega a la pantalla de ajustes
                    navController?.navigate("ajustes")
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes"
                    )
                }
            }
        }
    )
}

//TopBar para conversaciones y grupos





//BottomBar
@Composable
fun MiBottomBar(navController: NavHostController) {
    // Necesitamos el estado de la ruta actual para marcar el ítem seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation {
        // Ítem de Chats
        BottomNavigationItem(
            selected = currentRoute == "contactos",
            onClick = {
                navController.navigate("contactos") {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_chats),
                    contentDescription = "Chats",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Chats") }
        )

        // Ítem de Foros
        BottomNavigationItem(
            selected = currentRoute == "foro",
            onClick = {
                navController.navigate("foro") {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painterResource(Res.drawable.ic_foros),
                    contentDescription = "Foro",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Foro") }
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
                // Se muestra el botón de retroceso en esta pantalla
                DefaultTopBar(title = "Usuarios", navController = navController, showBackButton = true)
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centrar contenido en pantallas grandes
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
                                            Text(
                                                "Nombre: ${usuario.getNombreCompleto()}",
                                                style = MaterialTheme.typography.subtitle1
                                            )
                                            Text(
                                                "Alias: ${usuario.getAlias()}",
                                                style = MaterialTheme.typography.body1
                                            )
                                            Text(
                                                "Activo: ${usuario.getActivo()}",
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
                // Aquí se muestra el botón de retroceso
                DefaultTopBar(title = "Registro", navController = navController, showBackButton = true, muestraEngranaje = false)
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centrar contenido en pantallas grandes
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
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
                                errorMessage =
                                    if (password == confirmPassword && password.isNotBlank()) {
                                        // Lógica real de registro
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
    }
}

// --- Pantalla Login ---
@Composable
@Preview
fun pantallaLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Iniciar Sesión", navController = navController, showBackButton = true, muestraEngranaje = false)
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centrar contenido en pantallas grandes
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
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
    }
}

// --- Restablecer Contraseña ---
@Composable
@Preview
fun restableceContrasenna(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Restablecer Contraseña", navController = navController, showBackButton = true, muestraEngranaje = false)
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centrar contenido en pantallas grandes
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
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
    }
}
// --- elemento chat ---
@Composable
fun ChatCard(conversacion:Conversacion, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable{
                if(conversacion.grupo){
                    // Navegar a la pantalla de chat de grupo
                    navController.navigate("mostrarChatGrupo/${conversacion.id}")
                }else {
                    // Navegar a la pantalla de chat individual
                    navController.navigate("mostrarChat/${conversacion.id}")
                }
            },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID Chat: ${conversacion.id}")
            Text(text = "Participantes: ${conversacion.participants.joinToString()}")
            Text(text = "Número de mensajes: ${conversacion.messages.size}")
        }
    }
}



// --- Chats PorDefecto ---
@Composable
fun muestraChats(navController: NavHostController) {
    val listaChats = UsuarioPrincipal.getChatUser().conversaciones

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Chats",
                    navController = navController,
                    showBackButton = false,
                    irParaAtras = false,
                    muestraEngranaje = true
                )
            },
            bottomBar = {
                MiBottomBar(navController)
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    items(listaChats) { conversacion ->
                        ChatCard(conversacion = conversacion, navController = navController)
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
                        Icon(Icons.Default.Person, contentDescription = "Nuevo")
                    }
                }
            }
        }
    }
}




// --- Contactos ---
@Composable
@Preview
fun muestraContactos(navController: NavHostController, contactos: List<Usuario>) {
    val usuarios = contactos

    var showContactoDialog by remember { mutableStateOf(false) }
    var idContacto by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }

    var showChatDialog by remember { mutableStateOf(false) }
    // Lista mutable para los contactos seleccionados para el chat.
    val selectedContacts = remember { mutableStateListOf<Usuario>() }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Contactos",
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
                                            "Nombre: ${usuario.getNombreCompleto()}",
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                        Text(
                                            "Alias: ${usuario.getAlias()}",
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
                        Text("Nuevo Contacto")
                    }
                    Button(
                        onClick = {
                            showChatDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Nuevo Chat")
                    }
                }
                // AlertDialog para "Nuevo Contacto"
                if (showContactoDialog) {
                    AlertDialog(
                        onDismissRequest = { showContactoDialog = false },
                        title = { Text(text = "Nuevo Contacto") },
                        text = {
                            Column {
                                Text("Introduce el idContacto:")
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    label = { Text("idContacto") }
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    idContacto = inputText
                                    showContactoDialog = false
                                    // Aquí puedes agregar lógica adicional para procesar idContacto
                                }
                            ) {
                                Text("Guardar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showContactoDialog = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
                // AlertDialog para "Nuevo Chat" con selección múltiple de contactos
                if (showChatDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showChatDialog = false
                            selectedContacts.clear()
                        },
                        title = { Text(text = "Nuevo Chat") },
                        text = {
                            Column {
                                Text("Selecciona los contactos para el chat:")
                                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                                    items(usuarios) { usuario ->
                                        // Cada fila muestra el nombre y un checkbox para seleccionar/deseleccionar.
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
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    // Coge los IDs de los usuarios seleccionados.
                                    val participantIds = selectedContacts.map { it.getIdUnico() }
                                    // falta incluir el ID del usuario actual .
                                    // Ejemplo de creación de conversación:
                                    // val nuevaConversacion = Conversacion(
                                    //     id = UUID.randomUUID().toString(),
                                    //     participants = participantIds
                                    // )
                                    // función o navegar a otra pantalla.
                                    showChatDialog = false
                                    selectedContacts.clear()
                                }
                            ) {
                                Text("Crear Chat")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showChatDialog = false
                                    selectedContacts.clear()
                                }
                            ) {
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
@Composable
fun UsuCard(usuario: Usuario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable( onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${usuario.getNombreCompleto()}")
            Text(text = "Alias Público: ${usuario.getAlias()}")
            Text(text = "Alias Privado: ${usuario.getAliasPrivado()}")
        }
    }
}


// --- Ajustes ---
@Composable
@Preview()
fun muestraAjustes(navController: NavHostController = rememberNavController()) {
    val user = UsuarioPrincipal
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Ajustes",
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UsuCard(
                            usuario = user,
                            onClick = {
                                navController.navigate("mostrarPerfilPrincipal")
                            }
                        )

                        Button(
                            onClick = { navController.navigate("cambiarTema") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cambiar Modo Oscuro / Tea")
                        }
                        Button(
                            onClick = { /* Cambiar Idioma */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cambiar Fuente")
                        }
                        Button(
                            onClick = { /* Eliminar Chats */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("(Eliminar Chats)")
                        }
                        Button(
                            onClick = { /* Control de Cuentas */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("(Control de Cuentas)")
                        }
                        Button(
                            onClick = { navController.navigate("ajustesAyuda") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ayuda")
                        }
                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar Sesión")
                        }
                    }
                }
            }
        }
    }
}


// --- Mostrar Perdil ---
@Composable
fun mostrarPerfil(navController: NavHostController, usuario: Usuario?) {
    val usuario = usuario

    // dialogs
    var showDialogNombre by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }
    var showDialogEmail by remember { mutableStateOf(false) }
    var nuevoEmail by remember { mutableStateOf("") }


    // campos usuario
    var aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivado() ?: "") }
    var aliasPublico by remember { mutableStateOf(usuario?.getAlias() ?: "") }
    var descripcion by remember { mutableStateOf(usuario?.getDescripcion() ?: "") }
    var nombre by remember { mutableStateOf(usuario?.getNombreCompleto() ?: "") }
    var email by remember { mutableStateOf(usuario?.getCorreo() ?: "") }
    var isNameVisible by remember { mutableStateOf(false) }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Perfil",
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (usuario == null) {
                            Text("Usuario no encontrado")
                        } else {
                            // Alias
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = aliasPrivado,
                                    onValueChange = { aliasPrivado = it },
                                    label = { Text("Alias Privado") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = aliasPublico,
                                    onValueChange = { aliasPublico = it },
                                    label = { Text("Alias Público") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Descripción
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                            // Nombre: campo de solo lectura con opción para modificar mediante Dialog
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = nombre,
                                    onValueChange = { /* No se edita directamente */ },
                                    label = { Text("Nombre") },
                                    modifier = Modifier.weight(1f),
                                    readOnly = true,
                                    visualTransformation = if (isNameVisible) VisualTransformation.None else PasswordVisualTransformation()
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
                                        contentDescription = if (isNameVisible) "Ocultar nombre" else "Mostrar nombre"
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        nuevoNombre = nombre
                                        showDialogNombre = true
                                    }
                                ) {
                                    Text("Modificar")
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = {},
                                    label = { Text("Email") },
                                    readOnly = true,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = {
                                        nuevoEmail = email
                                        showDialogEmail = true
                                    }
                                ) {
                                    Text("Modificar")
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
                                            setAliasPrivado(aliasPrivado)
                                            setAlias(aliasPublico)
                                            setDescripcion(descripcion)
                                            setNombreCompleto(nombre)
                                            setCorreo(email)
                                        }
                                        navController.popBackStack()
                                    }
                                ) {
                                    Text("Aplicar")
                                }
                                Button(onClick = { navController.popBackStack() }) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showDialogEmail) {
        AlertDialog(
            onDismissRequest = { showDialogEmail = false },
            title = { Text("Modificar Email") },
            text = {
                OutlinedTextField(
                    value = nuevoEmail,
                    onValueChange = { nuevoEmail = it },
                    label = { Text("Nuevo email") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        email = nuevoEmail
                        showDialogEmail = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialogEmail = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Cambiar a cotraseña
    if (showDialogNombre) {
        AlertDialog(
            onDismissRequest = { showDialogNombre = false },
            title = { Text("Modificar Nombre") },
            text = {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text("Nuevo nombre") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        nombre = nuevoNombre  // Actualiza el campo nombre
                        showDialogNombre = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialogNombre = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}






//Mostrar chat entre dos personas, se podria mejorar pasandole una conversacion en vez de id del chat
@Composable
fun mostrarChat(navController: NavHostController, chatId : String?) {
    // Obtienes la lista de conversaciones y buscas la conversacion que tenga el id pasado
    val listaChats = UsuarioPrincipal.getChatUser().conversaciones
    val chat = listaChats.find { it.id == chatId } ?: return
    // Obtener el nombre del primer participante
    val participant1Name = chat.participants[1]

    // Estado para el mensaje que se está escribiendo
    var mensajeNuevo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = participant1Name, // Mostramos el nombre del participante1
                navController = navController,
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
            // Sección de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(chat.messages) { mensaje ->
                    // Dependiendo del senderId, izquierda o derecha
                    val isParticipant1 = mensaje.senderId == participant1Name
                    val alignment = if (isParticipant1) Alignment.CenterStart else Alignment.CenterEnd

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = alignment
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .widthIn(max = 250.dp)
                        ) {
                            Text(text = mensaje.content)
                        }
                    }
                }
            }

            // Barra de abajo
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
                    placeholder = { Text("Escribe...") }
                )
                IconButton(
                    onClick = {
                        // Aquí iría la lógica para enviar el mensaje
                        // (por ejemplo, actualizar la lista de mensajes en tu ViewModel)
                        // y luego limpiar el campo de texto
                        // Ejemplo:
                        // enviarMensaje(conversacion.id, participant1Name, participant2Name, mensajeNuevo)
                        mensajeNuevo = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar"
                    )
                }
            }
        }
    }
}

//Mostrar chatGrupo
@Composable
fun mostrarChatGrupo(navController: NavHostController, chatId: String?) {
    // Recorrer la lista de chats y buscar el chat cuya conversación tenga el id pasado
    val listaChats = UsuarioPrincipal.getChatUser().conversaciones
    val chat = listaChats.find { it.id == chatId } ?: return
    // Definir un título para el grupo (puedes modificarlo según tus necesidades)
    val groupTitle = "Grupo: ${chat.id}"

    // Estado para el mensaje que se está escribiendo
    var mensajeNuevo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = groupTitle,
                navController = navController,
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
            // Sección de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(chat.messages) { mensaje ->
                    // En un chat de grupo se muestra el nombre del emisor junto al mensaje
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = mensaje.senderId,
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = mensaje.content,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }

            // Barra de escritura y botón de enviar
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
                    placeholder = { Text("Escribe...") }
                )
                IconButton(
                    onClick = {
                        // Aquí iría la lógica para enviar el mensaje al grupo
                        // Por ejemplo, actualizar la conversación en un ViewModel
                        mensajeNuevo = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar"
                    )
                }
            }
        }
    }
}






// --- Home Page ---
// En la HomePage NO se muestra el botón de retroceso.
@Composable
@Preview
fun muestraHomePage(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {

                DefaultTopBar( title = "Inicio", navController = null, showBackButton = false, muestraEngranaje = false, irParaAtras = false)

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
                            Text("Ir a Login")
                        }
                        Button(
                            onClick = { navController.navigate("registro") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ir a Registro")
                        }
                        Button(
                            onClick = { navController.navigate("contactos") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Contactos")
                        }
                        Button(
                            onClick = { navController.navigate("ajustes") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ajustes")
                        }
                        Button(
                            onClick = { navController.navigate("usuarios") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Usuarios")
                        }
                    }
                }
            }
        }
    }
}

// --- SpashScreen ---
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
                    painter = painterResource(Res.drawable.connexus
                    ),
                    contentDescription = "Ícono de la aplicación"
                )
            }
        }
    }
}

//--------------------------------------------------

// Si el email NO está en el sistema
@Composable
fun PantallaEmailNoEnElSistema(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Email no existe", navController = navController, showBackButton = true, muestraEngranaje = true, irParaAtras = true)
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
                        Text("La dirección de correo no está registrada.", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Verifica que hayas escrito bien tu correo o regístrate.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.navigate("registro") }) {
                            Text("Ir a Registro")
                        }
                    }
                }
            }
        }
    }
}

// Si el email está en el sistema
@Composable
fun PantallaEmailEnElSistema(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Email en el Sistema", navController = navController, showBackButton = true, muestraEngranaje = true, irParaAtras = true)
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
                        Text("Se ha enviado un código para restablecer la contraseña a tu correo.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Código de Verificación") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                Text("Restablecer Contraseña")
                            }
                            Button(
                                onClick = { navController.navigate("login") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Pantalla de Restablecer Contraseña
@Composable
fun PantallaRestablecer(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Restablecer Contraseña", navController = navController, showBackButton = true, muestraEngranaje = true, irParaAtras = true)
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
                            contentDescription = "Ícono de la aplicación",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
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
                                        "Debes ingresar un correo"
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Enviar Correo")
                            }
                            Button(
                                onClick = { navController.navigate("login") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
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
                                Text("Debug: Restablecer OK")
                            }
                            Button(
                                onClick = { navController.navigate("emailNoEnSistema") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Restablecer FAIL")
                            }
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
    }
}

// Pantalla de Registro nuevo
@Composable
fun PantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Registro", navController = navController, showBackButton = true, muestraEngranaje = true, irParaAtras = true)
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
                            contentDescription = "Ícono de la aplicación",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                                            "Las contraseñas no coinciden o están vacías"
                                        }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Registrar")
                            }
                            Button(
                                onClick = { navController.navigate("login") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
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

// Pantalla de Login nuevo
@Composable
fun PantallaLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar( title = "Iniciar Sesión", navController = navController, showBackButton = false, irParaAtras = false, muestraEngranaje = false)
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centrar contenido en pantallas grandes
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier.padding(padding).padding(16.dp), // Aquí sí se aplica el `modifier`
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = "Ícono de la aplicación",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("restablecer") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("¿Olvidaste tu contraseña?")
                            }
                            Button(
                                onClick = { navController.navigate("registro") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Registrarse")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (email.isNotBlank() && password.isNotBlank()) {
                                    errorMessage = ""
                                    // Lógica real de autenticación
                                    //navController.navigate("home")

                                } else {
                                    errorMessage = "Debes completar ambos campos"
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Acceder")
                        }
                        //Spacer( modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("contactos") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ir a Contactos")
                            }
                            Button(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ajustes control cuentas")
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