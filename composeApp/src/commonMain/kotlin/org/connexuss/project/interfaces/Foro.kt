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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.encriptacion.EncriptacionSimetricaForo
import org.connexuss.project.encriptacion.toHex
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.ISecretosRepositorio
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseSecretosRepo
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Repositorio genérico instanciado
private val repoForo = SupabaseRepositorioGenerico()

// Clave simétrica sobreescibible para cada vez que se entra en un tema, se sobreescribe
object ClaveTemaHolder {
    var clave: AES.GCM.Key? by mutableStateOf(null)
}

// -----------------------
// Pantalla principal del foro
// -----------------------
@Composable
fun ForoScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var showNewTopicDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val refreshTrigger = remember { mutableStateOf(0) }
    val encHelper = remember { EncriptacionSimetricaForo() }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    val temasFlow = remember(refreshTrigger.value) {
        repoForo.getAll<Tema>(tablaTemas)
            .map { list -> list.filter { it.nombre.contains(searchText, ignoreCase = true) } }
    }

    // Flujos de temas y hilos
    //val temas by repoForo.getAll<Tema>(tablaTemas).collectAsState(initial = emptyList())
    val hilos by repoForo.getAll<Hilo>(tablaHilos).collectAsState(initial = emptyList())

    // Filtrar temas y contar hilos
    val filteredTemas = temasFlow.collectAsState(initial = emptyList()).value

    //val secretsRepo = remember { SupabaseSecretosRepo() }

    Scaffold(
        scaffoldState = scaffoldState,
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
                        Icon(Icons.Filled.Add, contentDescription = "Nuevo tema")
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
                            try {
                                // 1) Llamada simplificada: genera clave, inserta en vault y en temas
                                val temaResultado = encHelper.crearTemaSinPadding(
                                    nombrePlain  = nombre,
                                    secretsRpcRepo = SupabaseSecretosRepo()
                                )

                                // 2) Refresca UI y muestra notificación
                                refreshTrigger.value++
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Tema '$nombre' creado correctamente",
                                    duration = SnackbarDuration.Short
                                )
                            } catch (e: Exception) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Error al crear tema: ${e.message}",
                                    duration = SnackbarDuration.Long
                                )
                            } finally {
                                showNewTopicDialog = false
                            }
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
@OptIn(ExperimentalStdlibApi::class, ExperimentalEncodingApi::class)
@Composable
fun TemaScreen(
    navController: NavHostController,
    temaId: String,
    repoForo: SupabaseRepositorioGenerico,
    secretsRepo: ISecretosRepositorio
) {
    val scope = rememberCoroutineScope()
    var showNewThreadDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    //var aesKey by remember { mutableStateOf<AES.GCM.Key?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var nombrePlano by remember { mutableStateOf("(cargando tema…)") }

    // Instancia de tu helper
    //val encHelper = remember { EncriptacionSimplificada() }

    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    var claveLista by remember { mutableStateOf(false) }

    val temaObj by repoForo.getAll<Tema>(tablaTemas)
        .map { it.firstOrNull { t -> t.idTema == temaId } }
        .collectAsState(null)

    LaunchedEffect(temaId, temaObj) {
        if (temaObj == null) return@LaunchedEffect
        try {
            val secretoRpc = secretsRepo.recuperarSecretoSimpleRpc(temaId)
                ?: throw IllegalStateException("Secreto no disponible")
            // Decodificar clave RAW
            val keyBytes = Base64.decode(secretoRpc.decryptedSecret)
            val aesKey = CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)
            ClaveTemaHolder.clave = aesKey
            claveLista = true

            nombrePlano = aesKey.cipher()
                .decrypt(temaObj!!.nombre.hexToByteArray())
                .decodeToString()
        } catch (e: Exception) {
            nombrePlano = "(clave no disponible)"
        } finally {
            isLoading = false
        }
    }

    // 4) Flujos de hilos filtrados
    val hilos by remember(temaId, refreshTrigger) {
        repoForo.getAll<Hilo>(tablaHilos)
            .map { list -> list.filter { it.idTema == temaId } }
    }.collectAsState(initial = emptyList())

    // Carga inicial
    if (isLoading || temaObj == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nombrePlano) },
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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                items(hilos) { hilo ->
                    HiloCard(
                        hilo = hilo,
                        onClick = { navController.navigate("hilo/${hilo.idHilo}") }
                    )
                }
            }

            if (showNewThreadDialog) {
                CrearElementoDialog(
                    title = "Nuevo Hilo",
                    label = "Título del hilo",
                    onDismiss = { showNewThreadDialog = false },
                    onConfirm = { titulo ->
                        scope.launch {
                            try {
                                val key = ClaveTemaHolder.clave ?: throw IllegalStateException("Clave no lista")
                                // Ciframos con nonce incluido
                                val encryptedFull = key.cipher().encrypt(titulo.encodeToByteArray())
                                val tituloHex = encryptedFull.toHex()
                                val nuevo = Hilo(
                                    idHilo = generateId(),
                                    nombre = tituloHex,
                                    idTema = temaId
                                )
                                repoForo.addItem("hilo", nuevo)
                                refreshTrigger++
                                showNewThreadDialog = false
                            } catch (e: Exception) {
                                println("Error creando hilo: ${e.message}")
                            }
                        }
                    }
                )
            }
        }
    }
}

// -----------------------
// Pantalla de un hilo y sus posts
// -----------------------
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HiloScreen(navController: NavHostController, hiloId: String) {
    val scope = rememberCoroutineScope()
    var contenido by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"
    val tablaPosts = "post"

    var nombrePlano by remember { mutableStateOf("(cargando tema…)") }
    var claveLista by remember { mutableStateOf(false) }

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

    LaunchedEffect(hiloId, hilo) {
        if (hilo == null) return@LaunchedEffect
        nombrePlano = try {
            hilo!!.nombre?.let {
                ClaveTemaHolder.clave?.cipher()
                    ?.decrypt(it.hexToByteArray())
                    ?.decodeToString()
            } ?: "(clave no disponible)"
        } catch (e: Exception) {
            "(clave no disponible)"
        } finally {
            claveLista = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nombrePlano) },
                navigationIcon = { BackButton(navController) }
            )
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
                Divider()
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

                                val key = ClaveTemaHolder.clave ?: throw IllegalStateException("Clave no lista")
                                // Ciframos con nonce incluido
                                val encryptedFull = key.cipher().encrypt(contenido.encodeToByteArray())
                                val contenidoHex = encryptedFull.toHex()

                                UsuarioPrincipal?.let {
                                    val post = Post(
                                        idPost = generateId(),
                                        content = contenidoHex,
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
//@Composable
//fun TemaCard(tema: Tema, hilosCount: Int, onTemaClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onTemaClick),
//        elevation = 4.dp,
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = tema.nombre, style = MaterialTheme.typography.h6)
//            Spacer(Modifier.height(4.dp))
//            Text(
//                text = "$hilosCount ${if (hilosCount==1) "hilo" else "hilos"}",
//                style = MaterialTheme.typography.caption
//            )
//        }
//    }
//}

/**
 * Tarjeta para mostrar un Tema cifrado.
 * Obtiene la clave simétrica desde vault.secrets y desencripta el nombre.
 */
@OptIn(ExperimentalStdlibApi::class, ExperimentalEncodingApi::class)
@Composable
fun TemaCard(
    tema: Tema,
    hilosCount: Int,
    onTemaClick: () -> Unit,
    secretsRepo: ISecretosRepositorio = remember { SupabaseSecretosRepo() }
) {
    var nombrePlano by remember { mutableStateOf("(cargando…)") }

    val encHelper = remember { EncriptacionSimetricaForo() }

    val scope = rememberCoroutineScope()

    val secretoRepositorio = remember { SupabaseSecretosRepo() }

    scope.launch {
        encHelper.leerTema(
            temaId = tema.idTema,
            secretsRpcRepo = secretoRepositorio,
        ).let { nombre ->
            nombrePlano = nombre
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTemaClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombrePlano, style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$hilosCount ${if (hilosCount == 1) "hilo" else "hilos"}",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

/**
 * Tarjeta para mostrar un hilo cifrado.
 * Carga la clave simétrica desde vault.secrets y desencripta el nombre del hilo.
 */
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HiloCard(
    hilo: Hilo,
    onClick: () -> Unit
) {

    // Estado para la clave AES reconstruida
    //var aesKey by remember { mutableStateOf<AES.GCM.Key?>(null) }
    // Estado para el nombre desencriptado
    var nombrePlano by remember { mutableStateOf("(cargando...)") }
    //val scope = rememberCoroutineScope()

    // Desencriptar el nombre del hilo
    LaunchedEffect(ClaveTemaHolder.clave, hilo.nombre) {
        if (ClaveTemaHolder.clave != null) {
            val cipherBytes = hilo.nombre?.hexToByteArray()
            val plainBytes  = cipherBytes?.let { ClaveTemaHolder.clave!!.cipher().decrypt(ciphertext = it) }
            if (plainBytes != null) {
                nombrePlano    = plainBytes.decodeToString()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombrePlano, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun PostItem(post: Post) {

    var nombrePlano by remember { mutableStateOf("(cargando...)") }

    LaunchedEffect(ClaveTemaHolder.clave, post.content) {
        if (ClaveTemaHolder.clave != null) {
            val cipherBytes = post.content.hexToByteArray()
            val plainBytes  = cipherBytes.let { ClaveTemaHolder.clave!!.cipher().decrypt(ciphertext = it) }
            nombrePlano    = plainBytes.decodeToString()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = post.aliaspublico, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(text = post.fechaPost.toString(), style = MaterialTheme.typography.caption)
            }
            Spacer(Modifier.height(8.dp))
            Text(text = nombrePlano, style = MaterialTheme.typography.body1)
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
    }
}