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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseHiloRepositorio
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseTemasRepositorio

// Repositorio genérico instanciado
private val repoForo = SupabaseRepositorioGenerico()

// -----------------------
// Pantalla principal del foro
// -----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForoScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = remember { SnackbarHostState() }
    var showNewTopicDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val refreshTrigger = remember { mutableStateOf(0) }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    val temasFlow = remember(refreshTrigger.value) {
        repoForo.getAll<Tema>(tablaTemas)
            .map { list -> list.filter { it.nombre.contains(searchText, ignoreCase = true) } }
    }

    // Flujos de temas y hilos
    val hilos by repoForo.getAll<Hilo>(tablaHilos).collectAsState(initial = emptyList())

    // Filtrar temas y contar hilos
    val filteredTemas = temasFlow.collectAsState(initial = emptyList()).value

    Scaffold(
        snackbarHost = { scaffoldState },
        topBar = {
            TopAppBar(
                title = { Text("Foro General") },
                actions = {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Buscar...") },
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(150.dp)
                    )
                    IconButton(onClick = { showNewTopicDialog = true }) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Nuevo tema"
                        )
                    }
                }
            )
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
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
                                hilosCount = hilos.count { it.idTema == tema.idTema },
                                onTemaClick = { navController.navigate("tema/${tema.idTema}") }
                            )
                        }
                    }
                }
            }

            if (showNewTopicDialog) {
                CrearElementoDialog(
                    title = "Nuevo Tema",
                    label = "Nombre del tema",
                    onDismiss = { showNewTopicDialog = false },
                    onConfirm = { nombre ->
                        scope.launch {
                            repoForo.addItem(tablaTemas, Tema(nombre = nombre))
                            refreshTrigger.value++
                            scaffoldState.showSnackbar(
                                message = "Tema '$nombre' creado",
                                duration = SnackbarDuration.Short
                            )
                            showNewTopicDialog = false
                        }
                    }
                )
            }
        }
    }
}

// -----------------------
// Pantalla de un tema y sus hilos
// -----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemaScreen(navController: NavHostController, temaId: String) {
    val scope = rememberCoroutineScope()
    var showNewThreadDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    // Flujo del tema y flujo de hilos filtrados
    val tema by repoForo
        .getItem<Tema>(tablaTemas) {
            scope.launch {
                select {
                    filter { eq("idtema", temaId) }
                }
            }
        }
        .collectAsState(initial = null)

    val hilosFlow = remember(temaId, refreshTrigger) {
        repoForo.getAll<Hilo>(tablaHilos)
            .map { list -> list.filter { it.idTema == temaId } }
    }
    val hilos by hilosFlow.collectAsState(initial = emptyList())

    // Si el tema es nulo, mostrar un indicador de carga
    when {
        tema == null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    val repoTemas = SupabaseTemasRepositorio()
    val temaBuscado = repoTemas.getTemaPorId(temaId).collectAsState(initial = null).value

    // Si el tema no es nulo, mostrar la lista de hilos
    when {
        tema != null -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            if (temaBuscado != null) {
                                Text(temaBuscado.nombre)
                            }
                        },
                        navigationIcon = { BackButton(navController) },
                        actions = {
                            IconButton(onClick = { showNewThreadDialog = true }) {
                                Icon(Icons.Filled.Add, contentDescription = "Nuevo hilo")
                            }
                        }
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
                            Text("Tema no encontrado")
                        }
                    } else {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(padding)
                                .padding(vertical = 8.dp)
                        ) {
                            items(hilos) { hilo ->
                                HiloCard(hilo = hilo) {
                                    navController.navigate("hilo/${hilo.idHilo}")
                                }
                            }
                        }
                    }

                    if (showNewThreadDialog && tema != null) {
                        CrearElementoDialog(
                            title = "Nuevo Hilo",
                            label = "Título del hilo",
                            onDismiss = { showNewThreadDialog = false },
                            onConfirm = { titulo ->
                                scope.launch {
                                    val nuevo = Hilo(idHilo = generateId(), nombre = titulo, idTema = temaId)
                                    repoForo.addItem(tablaHilos, nuevo)
                                    refreshTrigger++ // Incrementamos el trigger para refrescar la lista
                                    showNewThreadDialog = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// -----------------------
// Pantalla de un hilo y sus posts
// -----------------------
@Composable
fun HiloScreen(navController: NavHostController, hiloId: String, startRoute: String) {

    val hiloState = remember(hiloId) { HiloState(hiloId) }

    // Inicia la escucha al entrar en pantalla
    DisposableEffect(hiloState) {
        onDispose { scope.launch { hiloState.stop() } }
    }

    val scope = rememberCoroutineScope()
    var contenido by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Tablas de temas y hilos
    val tablaHilos = "hilo"
    val tablaPosts = "post"

    // Creamos el Flow dentro de un remember que observe el trigger
    val postsFlow = remember(hiloId, refreshTrigger) {
        repoForo
            .getAll<Post>(tablaPosts)
            .map { list -> list.filter { it.idHilo == hiloId } }
    }

    // Flujo de hilo (opcionalmente para título) y posts
    val hilo by repoForo.getItem<Hilo>(tablaHilos) {
        scope.launch {
            select {
                filter { eq("idhilo", hiloId) }
            }
        }
    }.collectAsState(initial = null)

    // Recogemos los posts del hilo
    val posts by postsFlow.collectAsState(initial = emptyList())

    if (hilo == null) {
        EmptyStateMessage("Hilo no encontrado")
        return
    }

    val repoForoDedicado = SupabaseHiloRepositorio()
    val hiloBuscado = repoForoDedicado.getHiloPorId(hiloId).collectAsState(initial = null).value

    Scaffold(
        topBar = {
            if (hiloBuscado != null) {
                hiloBuscado.nombre?.let {
                    HiloTopBar(
                        title = it,
                        navController = navController,
                        newPostsCount = hiloState.newPostsCount.value,
                        onRefresh = { hiloState.reset(); refreshTrigger++ },
                        showRefresh = true,
                        startRoute = startRoute
                    )
                }
            }
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        PostItem(post = post)
                    }
                }
                HorizontalDivider()
                // Sección para nuevo post
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
                        try {
                            require(
                                contenido.isNotBlank().or(contenido.isNotEmpty())
                            )
                            scope.launch {
                                UsuarioPrincipal?.let {
                                    val post = Post(
                                        idPost = generateId(),
                                        content = contenido,
                                        idHilo = hiloId,
                                        idFirmante = it.idUnico,
                                        aliaspublico = it.aliasPublico
                                    )
                                    repoForo.addItem(tablaPosts, post)
                                    refreshTrigger++ // Incrementamos el trigger para refrescar la lista
                                    contenido = ""
                                } ?: println("Error: UsuarioPrincipal es nulo")
                            }
                        } catch (e: Exception) {
                            println("Error al enviar el post: ${e.message}")
                        }
                    }) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

// -----------------------
// Diálogo reutilizable
// -----------------------
@Composable
fun CrearElementoDialog(
    title: String,
    label: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
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
                onClick = { if (text.isNotBlank()) onConfirm(text) },
                enabled = text.isNotBlank()
            ) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// -----------------------
// Componentes UI reutilizables
// -----------------------
@Composable
fun TemaCard(tema: Tema, hilosCount: Int, onTemaClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTemaClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.nombre, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$hilosCount ${if (hilosCount==1) "hilo" else "hilos"}",
                style = MaterialTheme.typography.labelMedium
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = hilo.nombre ?: "Hilo sin título", style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(2.dp))
            Text(text = "ID Tema: ${hilo.idTema}", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = post.aliaspublico, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(text = post.fechaPost.toString(), style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text(text = post.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BackButton(navController: NavHostController) {
    IconButton(onClick = { navController.navigateUp() }) {Icon(
        Icons.AutoMirrored.Rounded.ArrowBack,
        contentDescription = "Volver atrás"
    )
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}