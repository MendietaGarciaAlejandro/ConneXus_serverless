package org.connexuss.project.interfaces.sistema

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.*
import kotlinx.coroutines.delay

import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
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


//BottomBar
@Composable
fun MiBottomBar(navController: NavHostController) {
    // Necesitamos el estado de la ruta actual para marcar el ítem seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation {
        // Ítem de Chats
        BottomNavigationItem(
            selected = currentRoute == "chats",
            onClick = {
                navController.navigate("chats") {
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
            selected = currentRoute == "foros",
            onClick = {
                navController.navigate("foros") {
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
                    contentDescription = "Foros",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Foros") }
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
fun ChatCard(chatItem: ConversacionesUsuario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID Chat: ${chatItem.conversacion.id}")
            Text(text = "Participantes: ${chatItem.conversacion.participants.joinToString()}")
            Text(text = "Número de mensajes: ${chatItem.conversacion.messages.size}")
        }
    }
}


// --- Chats PorDefecto ---
@Composable
fun muestraChats(navController: NavHostController) {
    // Genera 10 salas de chat de ejemplo
    val dummyChatsRooms = List(10) { index ->
        Conversacion(
            id = "chatRoom_$index",
            participants = listOf("user${index + 1}", "user${(index + 2)}"),
            messages = listOf(
                Mensaje(
                    id = "msg${index}_1",
                    senderId = "user${index + 1}",
                    receiverId = "user${(index + 2)}",
                    content = "Hola, ¿cómo estás en chat $index?",
                    fechaMensaje = LocalDateTime(2023, 1, 1, 12, 0)
                ),
                Mensaje(
                    id = "msg${index}_2",
                    senderId = "user${(index + 2)}",
                    receiverId = "user${index + 1}",
                    content = "Todo bien, ¿y tú?",
                    fechaMensaje = LocalDateTime(2023, 1, 1, 12, 5)
                )
            )
        )
    }

    // Asocia cada sala de chat a un objeto ChatsUsers
    val dummyChatsUsers = dummyChatsRooms.mapIndexed { index, chatRoom ->
        ConversacionesUsuario(
            id = "chatsUsers_$index",
            idUser = "user${index + 1}",
            conversacion = chatRoom
        )
    }

    // Se inicializa la lista mutable con los chats generados
    val listaChats = remember { mutableStateListOf<ConversacionesUsuario>().apply { addAll(dummyChatsUsers) } }

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

                    items(listaChats) { chatItem ->
                        ChatCard(chatItem = chatItem)
                    }
                }
                // Creo una caja con un botón flotante para mostrar contactos con un margen del tamaño de la botom bar
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
            // Caja principal que contiene la lista y los botones superpuestos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Caja para la lista de usuarios, con un padding superior para dejar espacio a los botones
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp) // Ajusta este valor según el alto que necesites para los botones
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
                // Row con botones superpuestos en la parte superior
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .widthIn(max = 800.dp)
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { /* Mostrar todos los contactos */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Nuevo Contacto")
                    }
                    Button(
                        onClick = { /* Mostrar contactos activos */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Nuevo Chat")
                    }
                }
            }
        }
    }
}

@Composable
fun GeneraUsuarios(): SnapshotStateList<Usuario> {
    val almacenamientoUsuario = remember { AlmacenamientoUsuario() }
    val usuarios = remember { mutableStateListOf<Usuario>() }

    LaunchedEffect(Unit) {
        try {
            // Usuarios iniciales
            val user1 = UtilidadesUsuario().instanciaUsuario(
                "Juan Perez",
                25,
                "paco@jerte.org",
                "pakito58",
                true
            )
            val user2 = UtilidadesUsuario().instanciaUsuario(
                "Maria Lopez",
                30,
                "marii@si.se",
                "marii",
                true
            )
            val user3 = UtilidadesUsuario().instanciaUsuario(
                "Pedro Sanchez",
                40,
                "roba@espannoles.es",
                "roba",
                true
            )

// Agregamos estos primeros usuarios al almacenamiento
            almacenamientoUsuario.agregarUsuario(user1)
            almacenamientoUsuario.agregarUsuario(user2)
            almacenamientoUsuario.agregarUsuario(user3)

// A continuación se generan más usuarios de ejemplo, hasta completar 50.
// Los datos (nombre, edad, email, username) son ficticios y se reparten de forma arbitraria.

            val user4 = UtilidadesUsuario().instanciaUsuario(
                "Carla Montes",
                27,
                "carla.montes@example.com",
                "carlam27",
                true
            )
            val user5 = UtilidadesUsuario().instanciaUsuario(
                "Sofía Hernández",
                32,
                "sofia.hernandez@example.com",
                "sofia32",
                true
            )
            val user6 = UtilidadesUsuario().instanciaUsuario(
                "Pablo Ortiz",
                29,
                "pablo.ortiz@example.com",
                "pablo29",
                true
            )
            val user7 = UtilidadesUsuario().instanciaUsuario(
                "Lucía Ramos",
                22,
                "lucia.ramos@example.com",
                "luciar22",
                true
            )
            val user8 = UtilidadesUsuario().instanciaUsuario(
                "Sergio Blanco",
                45,
                "sergio.blanco@example.com",
                "sergiob45",
                true
            )
            val user9 = UtilidadesUsuario().instanciaUsuario(
                "Andrea Alarcón",
                35,
                "andrea.alarcon@example.com",
                "andrea35",
                true
            )
            val user10 = UtilidadesUsuario().instanciaUsuario(
                "Miguel Flores",
                19,
                "miguel.flores@example.com",
                "miguelf19",
                true
            )
            val user11 = UtilidadesUsuario().instanciaUsuario(
                "Sara González",
                31,
                "sara.gonzalez@example.com",
                "sarag31",
                true
            )
            val user12 = UtilidadesUsuario().instanciaUsuario(
                "David Medina",
                28,
                "david.medina@example.com",
                "davidm28",
                true
            )
            val user13 = UtilidadesUsuario().instanciaUsuario(
                "Elena Ruiz",
                26,
                "elena.ruiz@example.com",
                "elena26",
                true
            )
            val user14 = UtilidadesUsuario().instanciaUsuario(
                "Alberto Vega",
                43,
                "alberto.vega@example.com",
                "albertov43",
                true
            )
            val user15 = UtilidadesUsuario().instanciaUsuario(
                "Julia Rojas",
                37,
                "julia.rojas@example.com",
                "juliar37",
                true
            )
            val user16 = UtilidadesUsuario().instanciaUsuario(
                "Marcos Fernández",
                41,
                "marcos.fernandez@example.com",
                "marcos41",
                true
            )
            val user17 = UtilidadesUsuario().instanciaUsuario(
                "Daniela Muñoz",
                20,
                "daniela.munoz@example.com",
                "danimu20",
                true
            )
            val user18 = UtilidadesUsuario().instanciaUsuario(
                "Carlos Pérez",
                34,
                "carlos.perez@example.com",
                "carlosp34",
                true
            )
            val user19 = UtilidadesUsuario().instanciaUsuario(
                "Tamara Díaz",
                38,
                "tamara.diaz@example.com",
                "tamarad38",
                true
            )
            val user20 = UtilidadesUsuario().instanciaUsuario(
                "Gonzalo Márquez",
                24,
                "gonzalo.marquez@example.com",
                "gonzalom24",
                true
            )
            val user21 = UtilidadesUsuario().instanciaUsuario(
                "Patricia Soto",
                36,
                "patricia.soto@example.com",
                "patricias36",
                true
            )
            val user22 = UtilidadesUsuario().instanciaUsuario(
                "Raúl Campos",
                42,
                "raul.campos@example.com",
                "raulc42",
                true
            )
            val user23 = UtilidadesUsuario().instanciaUsuario(
                "Irene Cabrera",
                25,
                "irene.cabrera@example.com",
                "irene25",
                true
            )
            val user24 = UtilidadesUsuario().instanciaUsuario(
                "Rodrigo Luna",
                33,
                "rodrigo.luna@example.com",
                "rodrigo33",
                true
            )
            val user25 = UtilidadesUsuario().instanciaUsuario(
                "Nerea Delgado",
                29,
                "nerea.delgado@example.com",
                "neread29",
                true
            )
            val user26 = UtilidadesUsuario().instanciaUsuario(
                "Adrián Ramos",
                44,
                "adrian.ramos@example.com",
                "adrianr44",
                true
            )
            val user27 = UtilidadesUsuario().instanciaUsuario(
                "Beatriz Calderón",
                27,
                "beatriz.calderon@example.com",
                "beac27",
                true
            )
            val user28 = UtilidadesUsuario().instanciaUsuario(
                "Ximena Navarro",
                23,
                "ximena.navarro@example.com",
                "ximenan23",
                true
            )
            val user29 = UtilidadesUsuario().instanciaUsuario(
                "Felipe Lara",
                39,
                "felipe.lara@example.com",
                "felipel39",
                true
            )
            val user30 = UtilidadesUsuario().instanciaUsuario(
                "Olga Martín",
                21,
                "olga.martin@example.com",
                "olgam21",
                true
            )
            val user31 = UtilidadesUsuario().instanciaUsuario(
                "Diego Castillo",
                48,
                "diego.castillo@example.com",
                "diegoc48",
                true
            )
            val user32 = UtilidadesUsuario().instanciaUsuario(
                "Alicia León",
                26,
                "alicia.leon@example.com",
                "alicia26",
                true
            )
            val user33 = UtilidadesUsuario().instanciaUsuario(
                "Tomás Vázquez",
                54,
                "tomas.vazquez@example.com",
                "tomasv54",
                true
            )
            val user34 = UtilidadesUsuario().instanciaUsuario(
                "Natalia Espinosa",
                29,
                "natalia.espinosa@example.com",
                "nataliae29",
                true
            )
            val user35 = UtilidadesUsuario().instanciaUsuario(
                "Esteban Gil",
                51,
                "esteban.gil@example.com",
                "estebang51",
                true
            )
            val user36 = UtilidadesUsuario().instanciaUsuario(
                "Rosa Ibáñez",
                30,
                "rosa.ibanez@example.com",
                "rosai30",
                true
            )
            val user37 = UtilidadesUsuario().instanciaUsuario(
                "Luis Morales",
                55,
                "luis.morales@example.com",
                "luism55",
                true
            )
            val user38 = UtilidadesUsuario().instanciaUsuario(
                "Diana Acosta",
                33,
                "diana.acosta@example.com",
                "dianaa33",
                true
            )
            val user39 = UtilidadesUsuario().instanciaUsuario(
                "Javier Ponce",
                46,
                "javier.ponce@example.com",
                "javiers46",
                true
            )
            val user40 = UtilidadesUsuario().instanciaUsuario(
                "Carmen Rivero",
                28,
                "carmen.rivero@example.com",
                "carmen28",
                true
            )
            val user41 = UtilidadesUsuario().instanciaUsuario(
                "Andrés Silva",
                47,
                "andres.silva@example.com",
                "andress47",
                true
            )
            val user42 = UtilidadesUsuario().instanciaUsuario(
                "Laura Sancho",
                24,
                "laura.sancho@example.com",
                "lauras24",
                true
            )
            val user43 = UtilidadesUsuario().instanciaUsuario(
                "Gabriel Escudero",
                31,
                "gabriel.escudero@example.com",
                "gabriele31",
                true
            )
            val user44 = UtilidadesUsuario().instanciaUsuario(
                "Dario Esparza",
                36,
                "dario.esparza@example.com",
                "darioe36",
                true
            )
            val user45 = UtilidadesUsuario().instanciaUsuario(
                "Verónica Salazar",
                25,
                "veronica.salazar@example.com",
                "veronicas25",
                true
            )
            val user46 = UtilidadesUsuario().instanciaUsuario(
                "Ernesto Herrera",
                34,
                "ernesto.herrera@example.com",
                "ernestoh34",
                true
            )
            val user47 = UtilidadesUsuario().instanciaUsuario(
                "Isabel Fuentes",
                26,
                "isabel.fuentes@example.com",
                "isabelf26",
                true
            )
            val user48 = UtilidadesUsuario().instanciaUsuario(
                "Matías Rosales",
                38,
                "matias.rosales@example.com",
                "matiasr38",
                true
            )
            val user49 = UtilidadesUsuario().instanciaUsuario(
                "Lorena Dávila",
                23,
                "lorena.davila@example.com",
                "lorenad23",
                true
            )
            val user50 = UtilidadesUsuario().instanciaUsuario(
                "Santiago Ibarra",
                44,
                "santiago.ibarra@example.com",
                "santi44",
                true
            )

// Agregamos los usuarios recién creados al almacenamiento
            almacenamientoUsuario.agregarUsuario(user4)
            almacenamientoUsuario.agregarUsuario(user5)
            almacenamientoUsuario.agregarUsuario(user6)
            almacenamientoUsuario.agregarUsuario(user7)
            almacenamientoUsuario.agregarUsuario(user8)
            almacenamientoUsuario.agregarUsuario(user9)
            almacenamientoUsuario.agregarUsuario(user10)
            almacenamientoUsuario.agregarUsuario(user11)
            almacenamientoUsuario.agregarUsuario(user12)
            almacenamientoUsuario.agregarUsuario(user13)
            almacenamientoUsuario.agregarUsuario(user14)
            almacenamientoUsuario.agregarUsuario(user15)
            almacenamientoUsuario.agregarUsuario(user16)
            almacenamientoUsuario.agregarUsuario(user17)
            almacenamientoUsuario.agregarUsuario(user18)
            almacenamientoUsuario.agregarUsuario(user19)
            almacenamientoUsuario.agregarUsuario(user20)
            almacenamientoUsuario.agregarUsuario(user21)
            almacenamientoUsuario.agregarUsuario(user22)
            almacenamientoUsuario.agregarUsuario(user23)
            almacenamientoUsuario.agregarUsuario(user24)
            almacenamientoUsuario.agregarUsuario(user25)
            almacenamientoUsuario.agregarUsuario(user26)
            almacenamientoUsuario.agregarUsuario(user27)
            almacenamientoUsuario.agregarUsuario(user28)
            almacenamientoUsuario.agregarUsuario(user29)
            almacenamientoUsuario.agregarUsuario(user30)
            almacenamientoUsuario.agregarUsuario(user31)
            almacenamientoUsuario.agregarUsuario(user32)
            almacenamientoUsuario.agregarUsuario(user33)
            almacenamientoUsuario.agregarUsuario(user34)
            almacenamientoUsuario.agregarUsuario(user35)
            almacenamientoUsuario.agregarUsuario(user36)
            almacenamientoUsuario.agregarUsuario(user37)
            almacenamientoUsuario.agregarUsuario(user38)
            almacenamientoUsuario.agregarUsuario(user39)
            almacenamientoUsuario.agregarUsuario(user40)
            almacenamientoUsuario.agregarUsuario(user41)
            almacenamientoUsuario.agregarUsuario(user42)
            almacenamientoUsuario.agregarUsuario(user43)
            almacenamientoUsuario.agregarUsuario(user44)
            almacenamientoUsuario.agregarUsuario(user45)
            almacenamientoUsuario.agregarUsuario(user46)
            almacenamientoUsuario.agregarUsuario(user47)
            almacenamientoUsuario.agregarUsuario(user48)
            almacenamientoUsuario.agregarUsuario(user49)
            almacenamientoUsuario.agregarUsuario(user50)

// Finalmente, añadimos todos los usuarios a la lista principal `usuarios`
            usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())

        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
    return usuarios
}

// --- Ajustes ---
@Composable
@Preview
fun muestraAjustes(navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(title = "Ajustes", navController = navController, showBackButton = true, irParaAtras = true,muestraEngranaje = false)
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
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { navController.navigate("cambiarTema") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cambiar Tema")
                        }
                        Button(
                            onClick = { /* Cambiar Idioma */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cambiar Idioma")
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
                        Spacer( modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("contactos") },
                        ) {
                            Text("Debug: Ir a Contactos")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ajustes control cuentas")
                            }
                            Button(
                                onClick = { navController.navigate("ajustesAyuda") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Debug: Ajustes ayuda/FAQ")
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