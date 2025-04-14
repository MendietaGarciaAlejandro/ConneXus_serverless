package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.SnackbarDuration
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.misc.*
import org.connexuss.project.supabase.SupabaseRepositorioGenerico


@Composable
fun ForoScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var showNewTopicDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Obtener temas observables del repositorio
    val allTemas = ForoRepository.obtenerTemas()
    val filteredTemas = if (searchText.isBlank()) {
        allTemas
    } else {
        allTemas.filter { it.nombre.contains(searchText, ignoreCase = true) }
    }

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
                when {
                    filteredTemas.isEmpty() && searchText.isBlank() ->
                        EmptyStateMessage("Presiona el + para crear un nuevo tema")

                    filteredTemas.isEmpty() ->
                        EmptyStateMessage("No se encontraron temas")

                    else -> LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTemas) { tema ->
                            TemaCard(
                                tema = tema,
                                hilosCount = HilosRepository.obtenerHilosPorTema(tema.idTema).size,
                                onTemaClick = {
                                    navController.navigate("tema/${tema.idTema}")
                                }
                            )
                        }
                    }
                }
            }

            // Dialogo para nuevo tema
            if (showNewTopicDialog) {
                CrearElementoDialog(
                    onDismiss = { showNewTopicDialog = false },
                    onConfirm = { nombre ->
                        scope.launch {
                            try {
                                val nuevoTema = Tema(nombre = nombre)
                                ForoRepository.agregarTema(nuevoTema)
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Tema '${nombre}' creado",
                                    duration = SnackbarDuration.Short
                                )
                            } catch (e: Exception) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Error al crear tema: ${e.message}",
                                    duration = SnackbarDuration.Long
                                )
                            }
                            showNewTopicDialog = false
                        }
                    },
                    title = "Nuevo Tema",
                    label = "Introduce el nombre del tema",
                    confirmText = "Crear",
                    cancelText = "Cancelar"
                )
            }
        }
    }
}

fun PostgrestQueryBuilder.eq(column: String, value: Any): PostgrestQueryBuilder {
    // Aquí se utiliza el método filter pasando "eq" como operador de igualdad.
    return this.filter(column, value)
}

fun PostgrestQueryBuilder.filter(
    column: String,
    value: Any
): PostgrestQueryBuilder {
    // Aquí se implementa la lógica para filtrar los resultados.
    return this.eq(column, value)
}

@Composable
fun TemaScreen(navController: NavHostController, temaId: String) {
    val scope = rememberCoroutineScope()
    val repo = SupabaseRepositorioGenerico()
    var tema by remember { mutableStateOf<Tema?>(null) }
    var hilos by remember { mutableStateOf<List<Hilo>>(emptyList()) }
    var showNewThreadDialog by remember { mutableStateOf(false) }

    // Cargar tema y sus hilos al iniciar la pantalla
    LaunchedEffect(temaId) {
        // Obtener el tema específico
        repo.getItem<Tema>("temas") {
            eq("idtema", temaId)
        }.collect { result ->
            // Aquí asignas el resultado a la variable 'tema'
            tema = result
        }

        // Obtener todos los hilos y filtrar por el tema actual
        repo.getAll<Hilo>("hilo").collect { allHilos ->
            hilos = allHilos.filter { it.idTema == temaId }
        }
    }

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
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hilos) { hilo ->
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
                        scope.launch {
                            val nuevoHilo = Hilo(
                                idHilo = generateId(),
                                nombre = titulo,
                                idTema = tema!!.idTema
                            )
                            repo.addItem("hilo", nuevoHilo)
                            hilos = hilos + nuevoHilo
                            showNewThreadDialog = false
                        }
                    },
                    title = "Crear nuevo hilo",
                    label = "Título del hilo"
                )
            }
        }
    }
}

@Composable
fun HiloScreen(navController: NavHostController, hiloId: String, repo: SupabaseRepositorioGenerico) {
    val hilos = remember { mutableStateListOf<Hilo>() }
    val hilo by remember { mutableStateOf(HilosRepository.hilos.find { it.idHilo == hiloId }) }
    val postsDelHilo by remember { mutableStateOf(PostsRepository.obtenerPostsPorHilo(hiloId)) }
    val posts = remember { mutableStateListOf<Post>() }

    // Filtrar el hilo actual basado en hiloId
    val hiloActual = hilos.find { it.idHilo == hiloId }

    // Cargar los hilos y posts desde Supabase
    LaunchedEffect(Unit) {
        // Obtener el hilo específico
        repo.getItem<Hilo>("hilo") {
            eq("idhilo", hiloId)
        }.collect {
            hiloActual?.let {
                hilos.add(it)
            }
        }
    }


    if (hiloActual == null) {
        EmptyStateMessage("Hilo no encontrado")
        return
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = hiloActual.nombre ?: "Hilo",
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
                    items(posts.filter { it.idHilo == hiloId }) { post ->
                        PostItem(post = post)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Divider()
                NuevoPostSection(
                    hiloId = hiloId,
                    onNuevoPost = { contenido ->
                        val nuevoPost = Post(
                            idPost = generateId(),
                            content = contenido,
                            idHilo = hiloId,
                            idFirmante = "UsuarioActual",
                            aliaspublico = "AliasPublicoActual"
                        )
                        repo.addItem("posts", nuevoPost)
                    }
                )
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
fun TemaCard(
    tema: Tema,
    hilosCount: Int,
    onTemaClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTemaClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tema.nombre,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$hilosCount ${if (hilosCount == 1) "hilo" else "hilos"}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.secondaryVariant
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
            // Se muestra el nombre del hilo o un mensaje por defecto si es nulo
            Text(
                text = hilo.nombre ?: "Hilo sin título",
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra, por ejemplo, el id del tema relacionado.
            Text(
                text = "Tema: ${hilo.idTema}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
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
                    text = post.idFirmante,
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
fun NuevoPostSection(hiloId: String, onNuevoPost: suspend (String) -> Unit) {
    var contenido by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val repo = SupabaseRepositorioGenerico()

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
                scope.launch {
                    val nuevoPost = Post(
                        idPost = generateId(),
                        content = contenido,
                        idHilo = hiloId,  // Ahora se utiliza el parámetro
                        idFirmante = "UsuarioActual",
                        aliaspublico = "AliasPublicoActual"
                    )
                    repo.addItem("posts", nuevoPost)
                    contenido = "" // Limpiar el campo de texto
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Insertar Post")
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