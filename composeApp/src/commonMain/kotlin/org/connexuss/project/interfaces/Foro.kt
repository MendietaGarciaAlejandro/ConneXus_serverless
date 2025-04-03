package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.*
import org.connexuss.project.misc.ForoRepository
import org.connexuss.project.misc.temasHilosPosts

@Composable
fun ForoScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var showNewTopicDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val allTemas = ForoRepository.temas
    val filteredTemas = if (searchText.isBlank())
        allTemas
    else allTemas.filter { it.nombre.contains(searchText, ignoreCase = true) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CommonTopBar(
                title = "Foro General",
                navController = navController,
                showSearch = true,
                searchText = searchText,
                onSearchChanged = { searchText = it },
                onActionClick = { showNewTopicDialog = true }
            )
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LimitaTamanioAncho { modifier ->
                if (filteredTemas.isEmpty()) {
                    EmptyStateMessage("No hay temas disponibles")
                } else {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTemas) { tema ->
                            TemaCard(tema = tema) {
                                navController.navigate("tema/${tema.idTema}")
                            }
                        }
                    }
                }
            }
            if (showNewTopicDialog) {
                CrearElementoDialog(
                    onDismiss = { showNewTopicDialog = false },
                    onConfirm = { nombre ->
                        scope.launch {
                            val nuevoTema = Tema(
                                idUsuario = "UsuarioActual",
                                nombre = nombre,
                                hilos = emptyList()
                            )
                            ForoRepository.agregarTema(nuevoTema)
                            showNewTopicDialog = false
                            scaffoldState.snackbarHostState.showSnackbar("Tema creado con éxito")
                        }
                    },
                    title = "Crear nuevo tema",
                    label = "Nombre del tema"
                )
            }
        }
    }
}

@Composable
fun TemaScreen(navController: NavHostController, temaId: String) {
    var tema by remember { mutableStateOf(ForoRepository.temas.find { it.idTema == temaId }) }
    var showNewThreadDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = tema?.nombre ?: "Tema no encontrado",
                navController = navController,
                showBackButton = true,
                onActionClick = { showNewThreadDialog = true }
            )
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            if (tema == null) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tema no encontrado",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                Column(modifier = modifier.fillMaxSize().padding(padding)) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tema!!.hilos) { hilo ->
                            HiloCard(hilo = hilo) {
                                navController.navigate("hilo/${hilo.idHilo}")
                            }
                        }
                    }
                }
            }
            if (showNewThreadDialog && tema != null) {
                CrearElementoDialog(
                    onDismiss = { showNewThreadDialog = false },
                    onConfirm = { titulo ->
                        val nuevoHilo = Hilo(
                            idHilo = generateId(),
                            idForeros = listOf(tema!!.idUsuario),
                            nombre = titulo,
                            posts = emptyList()
                        )
                        val temaActualizado = tema!!.copy(hilos = tema!!.hilos + nuevoHilo)
                        ForoRepository.actualizarTema(temaActualizado)
                        tema = temaActualizado
                        showNewThreadDialog = false
                    },
                    title = "Crear nuevo hilo",
                    label = "Título del hilo"
                )
            }
        }
    }
}

@Composable
fun HiloScreen(navController: NavHostController, hiloId: String) {
    var hilo by remember {
        mutableStateOf(
            temasHilosPosts.flatMap { it.hilos }.find { it.idHilo == hiloId }
        )
    }
    if (hilo == null) {
        EmptyStateMessage("Hilo no encontrado")
        return
    }
    Scaffold(
        topBar = {
            CommonTopBar(
                title = hilo!!.nombre ?: "Hilo",
                navController = navController,
                showBackButton = true
            )
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(modifier = modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(hilo!!.posts) { post ->
                        PostItem(post = post)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Divider()
                NuevoPostSection { contenido ->
                    val nuevoPost = Post(
                        senderId = "UsuarioActual",
                        receiverId = "",
                        content = contenido
                    )
                    hilo = hilo!!.copy(posts = hilo!!.posts + nuevoPost)
                }
            }
        }
    }
}

@Composable
fun CrearElementoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    title: String,
    label: String,
    confirmText: String = "Crear",
    cancelText: String = "Cancelar"
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) onConfirm(text)
                },
                enabled = text.isNotBlank()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelText)
            }
        }
    )
}

@Composable
fun CommonTopBar(
    title: String,
    navController: NavHostController,
    showBackButton: Boolean = false,
    showSearch: Boolean = false,
    searchText: String = "",
    onSearchChanged: (String) -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) BackButton(navController)
        },
        actions = {
            if (showSearch) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchChanged,
                    placeholder = { Text("Buscar...") },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(150.dp)
                )
            }
            IconButton(onClick = onActionClick) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo tema")
            }
        }
    )
}

@Composable
fun TemaCard(tema: Tema, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.nombre, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Creado por: ${tema.idUsuario}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun HiloCard(hilo: Hilo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = hilo.nombre ?: "Hilo sin título", style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(4.dp))
            if (hilo.posts.isNotEmpty()) {
                Text(
                    text = hilo.posts.last().content,
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
fun PostItem(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    text = post.fechaPost.toString(),
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
        Button(
            onClick = {
                if (contenido.isNotBlank()) {
                    onNuevoPost(contenido)
                    contenido = ""
                }
            }
        ) {
            Text("Enviar")
        }
    }
}

@Composable
fun BackButton(navController: NavHostController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}