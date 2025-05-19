package org.connexuss.project.interfaces

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
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
                    title = "usuarios",
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
                        ElevatedButton(
                            onClick = { showContent = !showContent },
                            elevation = ButtonDefaults.elevatedButtonElevation()
                        ) {
                            Text(
                                text = traducir("mostrar_usuarios"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AnimatedVisibility(visible = showContent) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(usuarios) { usuario ->
                                    Modifier
                                        .fillMaxWidth()
                                    ElevatedCard(
                                        modifier = Modifier.animateItem(
                                            fadeInSpec = null,
                                            fadeOutSpec = null
                                        ),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                        ),
                                        elevation = CardDefaults.elevatedCardElevation()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${traducir("alias_label")} ${usuario.getAliasMio()}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "${traducir("activo_label")} ${usuario.getActivoMio()}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.8f
                                                )
                                            )
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
@Composable
@Preview
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

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(
                if (!estaBloqueado) Modifier.clickable {
                    println("🧭 Navegando a: $destino")
                    navController.navigate(destino)
                } else Modifier
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (estaBloqueado) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                color = if (estaBloqueado) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            nombresParticipantes?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (estaBloqueado) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            if (ultimoMensaje != null && !estaBloqueado) {
                Text(
                    text = ultimoMensaje.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


// --- Chats PorDefecto ---
@Composable
@Preview
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
            bottomBar = { MiBottomBar(navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("nuevo") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = traducir("nuevo_chat")
                    )
                }
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                }
            }
        }
    }
}


// --- Contactos ---
@Composable
@Preview
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
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(contactos) { usuario ->
                            val estaBloqueado = usuario.idUnico in usuariosBloqueados

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (!estaBloqueado)
                                            Modifier.clickable {
                                                navController.navigate("mostrarPerfilUsuario/${usuario.idUnico}")
                                            }
                                        else Modifier
                                    ),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = if (estaBloqueado)
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${traducir("nombre_label")}: ${usuario.getNombreCompletoMio()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (estaBloqueado)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${traducir("alias_label")}: ${usuario.getAliasMio()}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (estaBloqueado)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (estaBloqueado) {
                                        Text(
                                            text = "Bloqueado",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { showNuevoContactoDialog = true },
                            modifier = Modifier.weight(1f)
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
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Introduce el alias privado del usuario:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                OutlinedTextField(
                                    value = nuevoContactoId,
                                    onValueChange = { nuevoContactoId = it },
                                    label = { Text("Alias Privado") },
                                    modifier = Modifier.fillMaxWidth()
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
                                            val todosUsuarios =
                                                repo.getAll<Usuario>("usuario").first()
                                            val usuarioEncontrado =
                                                todosUsuarios.find { it.getAliasPrivadoMio() == aliasBuscado }

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

                                                repo.getAll<UsuarioContacto>("usuario_contacto")
                                                    .collect { lista ->
                                                        registrosContacto =
                                                            lista.filter { it.idUsuario == currentUserId }
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
                        },
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
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Selecciona participantes:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                contactos.forEach { usuario ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Checkbox(
                                            checked = contactosSeleccionados.contains(usuario.idUnico),
                                            onCheckedChange = {
                                                contactosSeleccionados = if (it)
                                                    contactosSeleccionados + usuario.idUnico
                                                else
                                                    contactosSeleccionados - usuario.idUnico
                                            }
                                        )
                                        Text(
                                            text = usuario.getNombreCompletoMio(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                if (contactosSeleccionados.size > 1) {
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
                            }) {
                                Text("Crear")
                            }
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
@Preview
fun UsuCard(usuario: Usuario, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen cuadrada a la izquierda
            usuario.getImagenPerfilMio()?.let { painterResource(it) }?.let {
                Surface(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    tonalElevation = 2.dp
                ) {
                    Image(
                        painter = it,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Información del usuario en una columna
            Column {
                Text(
                    text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${traducir("alias_publico")} ${usuario.getAliasMio()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${traducir("alias_privado")} ${usuario.getAliasPrivadoMio()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
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
fun muestraAjustes(
    navController: NavHostController = rememberNavController(),
    settingsState: SettingsState
) {
    val user = UsuarioPrincipal
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DefaultTopBar(
                title = "ajustes",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (user != null) {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            UsuCard(
                                usuario = user,
                                onClick = {
                                    navController.navigate("mostrarPerfilPrincipal")
                                }
                            )
                        }
                    }

                    // Opciones principales
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { navController.navigate("cambiarTema") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_modo_oscuro_tema"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("cambiaFuente") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_fuente"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("idiomas") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("cambiar_idioma"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    // Opciones de gestión
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { /* Eliminar Chats */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text(
                                    text = traducir("eliminar_chats"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("control_de_cuentas"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            FilledTonalButton(
                                onClick = { navController.navigate("ajustesAyuda") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = traducir("ayuda"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    // Botón de cerrar sesión
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    Supabase.client.auth.signOut()
                                    settingsState.clearSession()
                                    sesionActualUsuario = null
                                    UsuarioPrincipal = null
                                    println("🔒 Sesión local y remota cerrada")
                                } catch (e: Exception) {
                                    println("❌ Error cerrando sesión: ${e.message}")
                                }
                            }
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = traducir("cerrar_sesion"),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

val repo = SupabaseUsuariosRepositorio()

// --- Mostrar Perdil ---
/**
 * Composable que muestra el perfil del usuario.
 *
 * @param navController controlador de navegación.
 * @param usuarioU usuario a mostrar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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
                CenterAlignedTopAppBar(
                    title = { Text(text = traducir("perfil")) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = traducir("volver")
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
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
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (usuario == null) {
                            Text(
                                text = traducir("usuario_no_encontrado"),
                                style = MaterialTheme.typography.titleLarge
                            )
                        } else {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier.size(120.dp),
                                tonalElevation = 4.dp,
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            ) {
                                Image(
                                    painter = painterResource(imagenPerfilState.value),
                                    contentDescription = "Imagen de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            FilledTonalButton(
                                onClick = {
                                    val nuevaImagen = obtenerImagenAleatoria()
                                    imagenPerfilState.value = nuevaImagen
                                    usuario?.apply {
                                        setImagenPerfilMia(nuevaImagen)
                                        setImagenPerfilIdMia(obtenerClaveDesdeImagen(nuevaImagen))
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = traducir("cambiar"))
                            }

                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Alias
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = aliasPrivado,
                                            onValueChange = { aliasPrivado = it },
                                            label = { Text(text = traducir("alias_privado")) },
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                        OutlinedTextField(
                                            value = aliasPublico,
                                            onValueChange = { aliasPublico = it },
                                            label = { Text(text = traducir("alias_publico")) },
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                    }

                                    // Descripción
                                    OutlinedTextField(
                                        value = descripcion,
                                        onValueChange = { descripcion = it },
                                        label = { Text(text = traducir("descripcion")) },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 3,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    // Contraseña
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = contrasennia,
                                            onValueChange = { },
                                            label = { Text(text = traducir("nombre_label")) },
                                            modifier = Modifier.weight(1f),
                                            readOnly = true,
                                            visualTransformation = if (isNameVisible)
                                                VisualTransformation.None
                                            else
                                                PasswordVisualTransformation(),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                        IconButton(
                                            onClick = { isNameVisible = !isNameVisible }
                                        ) {
                                            Icon(
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
                                        FilledTonalButton(
                                            onClick = {
                                                nuevoNombre = contrasennia
                                                showDialogNombre = true
                                            }
                                        ) {
                                            Text(text = traducir("modificar"))
                                        }
                                    }

                                    // Email
                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = {},
                                        label = { Text(text = traducir("email")) },
                                        readOnly = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                                                    if (contrasennia != it.getContrasenniaMio()) {
                                                        Supabase.client.auth.updateUser {
                                                            password = contrasennia
                                                        }
                                                    }
                                                    repo.updateUsuario(it)
                                                    usuario = repo.getUsuarioAutenticado()
                                                    navController.popBackStack()
                                                } catch (e: Exception) {
                                                    println("Error al actualizar usuario: ${e.message}")
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = traducir("aplicar"))
                                }
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.weight(1f)
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
    if (showDialogNombre) {
        AlertDialog(
            onDismissRequest = { showDialogNombre = false },
            title = {
                Text(
                    text = traducir("modificar_nombre"),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text(text = traducir("nuevo_nombre")) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        contrasennia = nuevoNombre
                        usuario?.setContrasenniaMio(nuevoNombre)
                        showDialogNombre = false

                        usuario?.let {
                            coroutineScope.launch {
                                try {
                                    Supabase.client.auth.updateUser {
                                        password = nuevoNombre
                                    }
                                    repo.updateUsuario(it)
                                } catch (e: Exception) {
                                    println("❌ Error al cambiar contraseña: ${e.message}")
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

//Mostrar perfil usuario chat
/**
 * Composable que muestra el perfil de un usuario en el chat.
 *
 * @param navController controlador de navegación.
 * @param userId identificador único del usuario.
 * @param imagenesApp lista de imágenes de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = usuario?.getNombreCompletoMio() ?: "Perfil",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                Text(
                    text = "Usuario no encontrado",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            val aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivadoMio() ?: "") }
            val aliasPublico by remember { mutableStateOf(usuario?.getAliasMio() ?: "") }
            val descripcion by remember { mutableStateOf(usuario?.getDescripcionMio() ?: "") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imagenDrawable = remember(usuario?.getImagenPerfilIdMio()) {
                    obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio())
                }

                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tonalElevation = 4.dp,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ) {
                    Image(
                        painter = painterResource(imagenDrawable),
                        contentDescription = "Imagen de perfil de ${usuario?.getNombreCompletoMio()}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = aliasPrivado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Alias Privado") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        OutlinedTextField(
                            value = aliasPublico,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Alias Público") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            if (usuario != null) {
                                scope.launch {
                                    val repo = SupabaseRepositorioGenerico()
                                    try {
                                        val relaciones =
                                            repo.getAll<ConversacionesUsuario>("conversaciones_usuario")
                                                .first()
                                        val conversacionesComunes = relaciones
                                            .groupBy { it.idconversacion }
                                            .filter { (_, users) ->
                                                val ids = users.map { it.idusuario }
                                                currentUserId in ids && usuario!!.getIdUnicoMio() in ids && ids.size == 2
                                            }
                                            .keys

                                        println("🔍 Conversaciones individuales encontradas: $conversacionesComunes")

                                        for (convId in conversacionesComunes) {
                                            repo.deleteItem<ConversacionesUsuario>(
                                                tableName = "conversaciones_usuario",
                                                idField = "idconversacion",
                                                idValue = convId
                                            )
                                            repo.deleteItem<Conversacion>(
                                                tableName = "conversacion",
                                                idField = "id",
                                                idValue = convId
                                            )
                                            println("🗑️ Eliminada conversación $convId")
                                        }

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
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar Contacto")
                    }

                    FilledTonalButton(
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Bloquear Usuario")
                    }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun muestraHomePage(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = traducir("inicio"),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
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
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ElevatedButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(
                                text = traducir("ir_a_login"),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        ElevatedButton(
                            onClick = { navController.navigate("registro") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Text(
                                text = traducir("ir_a_registro"),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        FilledTonalButton(
                            onClick = { navController.navigate("contactos") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = traducir("contactos"),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        FilledTonalButton(
                            onClick = { navController.navigate("ajustes") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = traducir("ajustes"),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        FilledTonalButton(
                            onClick = { navController.navigate("usuarios") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = traducir("usuarios"),
                                style = MaterialTheme.typography.titleMedium
                            )
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
@Preview
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
            UsuarioPrincipal = usuario

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(Res.drawable.connexus),
                        contentDescription = "Ícono de la aplicación",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir(titleKey),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = traducir("volver")
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* acción engranaje */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = traducir("ajustes")
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = "Ícono de la aplicación",
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Text(
                        text = traducir(mensajeKey),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    if (mostrarCampoCodigo) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text(traducir("codigo_verificacion")) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { navController.navigate(rutaBotonPrincipal) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = traducir(textoBotonPrincipalKey),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        if (textoBotonSecundarioKey != null && rutaBotonSecundario != null) {
                            Button(
                                onClick = { navController.navigate(rutaBotonSecundario) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = traducir(textoBotonSecundarioKey),
                                    style = MaterialTheme.typography.labelLarge
                                )
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
@Preview
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
@Preview
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PantallaRegistro(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var emailInterno by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val repoSupabase = SupabaseUsuariosRepositorio()
    val errorContrasenas = traducir("error_contrasenas")
    val errorEmailYaRegistrado = traducir("error_email_ya_registrado")
    val usuario = Usuario()
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir("registro"),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = traducir("volver")
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(traducir("nombre")) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = emailInterno,
                        onValueChange = { emailInterno = it },
                        label = { Text(traducir("email")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(traducir("contrasena")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text(traducir("confirmar_contrasena")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

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

                                            val authResult =
                                                Supabase.client.auth.signUpWith(Email) {
                                                    this.email = emailTrimmed
                                                    this.password = password
                                                }
                                            navController.navigate("registroVerificaCorreo/${emailTrimmed}/${nombre}/${password}")

                                        } catch (e: Exception) {
                                            errorMessage = "❌ Error al registrar: ${e.message}"
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    traducir("registrar"),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                traducir("cancelar"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = traducir("verificacion_correo"),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                    modifier = modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 4.dp
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.connexus),
                            contentDescription = traducir("icono_app"),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = traducir("correo_enviado_a"),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = email ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = traducir("verifica_cuenta_continuar"),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = {
                            scope.launch {
                                try {
                                    if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                                        Supabase.client.auth.signInWith(Email) {
                                            this.email = email
                                            this.password = password
                                        }
                                    }

                                    val usuarioActual = Supabase.client.auth.currentUserOrNull()

                                    if (usuarioActual?.emailConfirmedAt != null) {
                                        val imagenAleatoria =
                                            UtilidadesUsuario().generarImagenPerfilAleatoria()

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
                                    navController.navigate("login")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = traducir("ya_verificado"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
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
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = traducir("reenviar_correo"),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    if (mensaje.isNotEmpty()) {
                        Text(
                            text = mensaje,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (mensaje.startsWith("❌")) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
