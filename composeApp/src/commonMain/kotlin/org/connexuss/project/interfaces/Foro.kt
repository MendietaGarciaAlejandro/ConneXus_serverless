package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema

/*
// --- Interfaces antiguas del Foro ---
@Composable
@Preview
fun muestraForo(navController: NavHostController) {
    val temas = remember { mutableStateListOf<String>() }
    var nuevoTema by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("foro"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = nuevoTema,
                        onValueChange = { nuevoTema = it },
                        label = { Text(traducir("nuevo_tema")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (nuevoTema.isNotEmpty()) {
                                temas.add(nuevoTema)
                                nuevoTema = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(traducir("agregar_tema"))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(temas) { tema ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = 4.dp
                            ) {
                                Text(
                                    tema,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Tema del Foro ---
@Composable
@Preview
fun muestraTemaForo(navController: NavHostController) {
    val mensajes = remember { mutableStateListOf<String>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("tema_del_foro"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = modifier.fillMaxSize().padding(padding)) {
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
                                    text = mensaje,
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
                                mensajes.add(nuevoMensaje)
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
}
 */

// Nuevas interfaces -----


// Interfaces en local para ver como se ve las pantallas
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PantallaForoLocal(
    navController: NavHostController
) {
    val temas = remember { mutableStateListOf<String>() }
    var nuevoTema by remember { mutableStateOf("") }

    Scaffold(
        topBar = { DefaultTopBar(title = traducir("foro"), navController = navController, showBackButton = true, irParaAtras = true, muestraEngranaje = true) },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nuevoTema,
                    onValueChange = { nuevoTema = it },
                    label = { Text(traducir("nuevo_tema")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (nuevoTema.isNotEmpty()) {
                            temas.add(nuevoTema)
                            nuevoTema = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(traducir("agregar_tema"))
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(temas) { tema ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp,
                            onClick = {
                                navController.navigate("tema/$tema")
                            }
                        ) {
                            Text(
                                tema,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PantallaTemaLocal(
    navController: NavHostController,
    temaParam: String
) {
    val mensajes = remember { mutableStateListOf<String>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = { DefaultTopBar(title = temaParam, navController = navController, showBackButton = true, irParaAtras = true) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(modifier = modifier.fillMaxSize().padding(padding)) {
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
                            elevation = 4.dp,
                            onClick = {
                                navController.navigate("hilo/$mensaje")
                            }
                        ) {
                            Text(
                                text = mensaje,
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
                            mensajes.add(nuevoMensaje)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PantallaHiloLocal(
    navController: NavHostController,
    mensajeParam: String
) {
    val mensajes = remember { mutableStateListOf<String>() }
    var nuevoMensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = { DefaultTopBar(title = mensajeParam, navController = navController, showBackButton = true, irParaAtras = true) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
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
                        elevation = 4.dp,
                        onClick = {
                            navController.navigate("hilo/$mensaje")
                        }
                    ) {
                        Text(
                            text = mensaje,
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
                        mensajes.add(nuevoMensaje)
                        nuevoMensaje = ""
                    }
                }) {
                    Text(traducir("enviar"))
                }
            }
        }
    }
}

// A pulir --

@Composable
fun ForoScreen(navController: NavHostController, temas: List<Tema>) {
    // Pantalla principal del foro, con un listado de Temas
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Foro General") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(temas) { tema ->
                    TemaCard(tema = tema, onClick = {
                        navController.navigate("tema/${tema.idTema}")
                    })
                }
            }
        }
    }
}

@Composable
fun TemaCard(tema: Tema, onClick: () -> Unit) {
    // Cada Card muestra el nombre del tema y opcionalmente otros detalles (último hilo, fecha, etc.)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.nombre, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            // Aquí podrías agregar detalles extra: autor, fecha de creación, descripción, etc.
            Text(
                text = "Creado por: ${tema.idUsuario}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TemaScreen(navController: NavHostController, tema: Tema) {
    // Pantalla que muestra los hilos del tema seleccionado.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = tema.nombre) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista de hilos
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tema.hilos) { hilo ->
                    HiloCard(hilo = hilo, onClick = {
                        navController.navigate("hilo/${hilo.idHilo}")
                    })
                }
            }
            Divider()
            // Sección para agregar un nuevo hilo (con título o mensaje inicial)
            NuevoHiloSection { nuevoHiloTitulo ->
                // Aquí gestionarías la creación de un nuevo hilo, actualizando el estado.
            }
        }
    }
}

@Composable
fun HiloCard(hilo: Hilo, onClick: () -> Unit) {
    // Card para representar un hilo. Puedes incluir el título y una previsualización del último post.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = hilo.nombre ?: "Hilo sin título",
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Ejemplo: previsualización del último post
            if (hilo.posts.isNotEmpty()) {
                val ultimoPost = hilo.posts.last()
                Text(
                    text = ultimoPost.content,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            } else {
                Text(
                    text = "Sin mensajes",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun NuevoHiloSection(onNuevoHilo: (String) -> Unit) {
    var tituloHilo by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = tituloHilo,
            onValueChange = { tituloHilo = it },
            label = { Text("Título del nuevo hilo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (tituloHilo.isNotBlank()) {
                    onNuevoHilo(tituloHilo)
                    tituloHilo = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Crear Hilo")
        }
    }
}

@Composable
fun HiloScreen(navController: NavHostController, hilo: Hilo) {
    // Pantalla que muestra los posts de un hilo, estilo conversación
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = hilo.nombre ?: "Hilo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(hilo.posts) { post ->
                    PostItem(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Divider()
            NuevoPostSection { nuevoContenido ->
                // Aquí se debe gestionar la creación de un nuevo post en el hilo
            }
        }
    }
}

@Composable
fun PostItem(post: Post) {
    // Representa un post en el hilo, con datos como el remitente, fecha y contenido.
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Podrías agregar una imagen de avatar o icono
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.senderId,
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${post.fechaPost.dayOfMonth.toString().padStart(2, '0')}/" +
                            "${post.fechaPost.monthNumber.toString().padStart(2, '0')}/" +
                            "${post.fechaPost.year} " +
                            "${post.fechaPost.hour.toString().padStart(2, '0')}:" +
                            "${post.fechaPost.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.caption
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.content, style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun NuevoPostSection(onNuevoPost: (String) -> Unit) {
    var contenido by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Nuevo mensaje") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (contenido.isNotBlank()) {
                onNuevoPost(contenido)
                contenido = ""
            }
        }) {
            Text("Enviar")
        }
    }
}


// --- Foro con Firebase ---
// para cuando se implemente el repositorio de Firebase
/*
@Composable
fun PantallaForo(
    navController: NavHostController,
    foroRepository: FirebaseForoRepositorio
) {
    val temas by foroRepository.getTemas().collectAsState(initial = emptyList())

    Scaffold(
        topBar = { DefaultTopBar(title = traducir("foro"), navController = navController, showBackButton = true, irParaAtras = true, muestraEngranaje = true) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(temas) { tema ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { navController.navigate("tema/${tema.idTema}") },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = tema.nombre ?: traducir("nuevo_tema"), style = MaterialTheme.typography.h6)
                        // Aquí podrías mostrar información adicional, por ejemplo, número de hilos
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaTema(
    navController: NavHostController,
    temaId: String,
    foroRepository: FirebaseForoRepositorio
) {
    val hilos by foroRepository.getHilos(temaId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = { DefaultTopBar(title = traducir("tema_del_foro"), navController = navController, showBackButton = true, irParaAtras = true) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(hilos) { hilo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { navController.navigate("hilo/${hilo.idHilo}") },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = hilo.nombre ?: "Hilo sin título", style = MaterialTheme.typography.subtitle1)
                        // Aquí podrías mostrar el número de posts, etc.
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaHilo(
    navController: NavHostController,
    hiloId: String,
    foroRepository: FirebaseForoRepositorio
) {
    val posts by foroRepository.getPosts(hiloId).collectAsState(initial = emptyList())
    var nuevoPost by remember { mutableStateOf("") }

    Scaffold(
        topBar = { DefaultTopBar(title = traducir("tema_del_foro"), navController = navController, showBackButton = true, irParaAtras = true) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = post.content, style = MaterialTheme.typography.body1)
                            // Muestra la fecha o autor si lo deseas
                        }
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
                    value = nuevoPost,
                    onValueChange = { nuevoPost = it },
                    label = { Text(traducir("nuevo_mensaje")) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (nuevoPost.isNotBlank()) {
                        // Crea y añade el nuevo post
                        val post = Post(
                            senderId = "UsuarioActual", // Reemplaza con el ID del usuario actual
                            receiverId = "", // En un hilo de foro no se usa este campo
                            content = nuevoPost
                        )
                        // Llama a la función de agregar el post en el repositorio
                        // Por ejemplo:
                        // scope.launch { foroRepository.addPost(hiloId, post) }
                        nuevoPost = ""
                    }
                }) {
                    Text(traducir("enviar"))
                }
            }
        }
    }
}
 */