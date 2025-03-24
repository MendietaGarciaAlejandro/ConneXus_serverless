package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.firebase.pruebas.FirebaseForoRepositorio
import org.jetbrains.compose.ui.tooling.preview.Preview

// --- Foro ---
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