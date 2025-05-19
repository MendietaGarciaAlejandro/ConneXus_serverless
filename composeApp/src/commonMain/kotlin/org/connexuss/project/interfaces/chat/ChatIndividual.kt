package org.connexuss.project.interfaces.chat

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.interfaces.navegacion.TopBarUsuario
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.instanciaSupabaseClient
import org.connexuss.project.supabase.subscribeTableAsFlow
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.DrawableResource

/**
 * Muestra el chat entre dos usuarios.
 *
 * Busca la conversación por su identificador y muestra los mensajes intercambiados.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación.
 */
@Composable
fun mostrarChat(navController: NavHostController, chatId: String?) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val supabaseClient = remember {
        instanciaSupabaseClient(
            tieneStorage = true,
            tieneAuth = true,
            tieneRealtime = true,
            tienePostgrest = true
        )
    }
    val scope = rememberCoroutineScope()

    var participantes by remember { mutableStateOf<List<String>>(emptyList()) }
    var otroUsuarioNombre by remember { mutableStateOf<String?>(null) }
    var otroUsuarioImagen by remember { mutableStateOf<DrawableResource?>(null) }
    var mensajeNuevo by remember { mutableStateOf("") }

    val todosLosMensajes by supabaseClient
        .subscribeTableAsFlow<Mensaje, String>(
            table = "mensaje",
            primaryKey = Mensaje::id,
            filter = null
        )
        .collectAsState(initial = emptyList())

    val mensajes = todosLosMensajes.filter { it.idconversacion == chatId }

    LaunchedEffect(chatId) {
        if (chatId == null) return@LaunchedEffect
        val repo = SupabaseRepositorioGenerico()

        val relaciones = repo.getAll<ConversacionesUsuario>("conversaciones_usuario").first()
        participantes = relaciones
            .filter { it.idconversacion == chatId }
            .map { it.idusuario }

        println("👥 Participantes cargados: $participantes")

        val otroUsuarioId = participantes.firstOrNull { it != currentUserId }
        if (otroUsuarioId != null) {
            val todosUsuarios = repo.getAll<Usuario>("usuario").first()
            val otroUsuario = todosUsuarios.find { it.getIdUnicoMio() == otroUsuarioId }
            otroUsuarioNombre = otroUsuario?.getNombreCompletoMio()
            //otroUsuarioImagen = otroUsuario?.getImagenPerfilMio()
            println("🙋 Nombre otro participante: $otroUsuarioNombre")
        }
    }

    if (chatId == null || participantes.isEmpty()) {
        ChatLoading(Modifier.fillMaxSize())
        return
    }

    Scaffold(
        topBar = {
            TopBarUsuario(
                title = otroUsuarioNombre ?: "Chat",
                profileImage = otroUsuarioImagen ?: Res.drawable.connexus,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false,
                onTitleClick = {
                    val otroUsuarioId = participantes.firstOrNull { it != currentUserId }
                    if (otroUsuarioId != null) {
                        navController.navigate("mostrarPerfilUsuario/$otroUsuarioId")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(mensajes.sortedBy { it.fechaMensaje }) { mensaje ->
                    val esMio = mensaje.idusuario == currentUserId
                    var expanded by remember { mutableStateOf(false) }
                    var showEditDialog by remember { mutableStateOf(false) }
                    var nuevoContenido by remember { mutableStateOf(mensaje.content) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (esMio) expanded = true
                                    }
                                )
                            },
                        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        BurbujaMensaje(mensaje = mensaje, esMio = esMio)

                        if (esMio) {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Editar") },
                                    onClick = {
                                        nuevoContenido = mensaje.content
                                        showEditDialog = true
                                        expanded = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    onClick = {
                                        scope.launch {
                                            supabaseClient
                                                .from("mensaje")
                                                .update({ set("content", "Mensaje eliminado") }) {
                                                    filter { eq("id", mensaje.id) }
                                                }
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }

                        if (showEditDialog) {
                            AlertDialog(
                                onDismissRequest = { showEditDialog = false },
                                title = { Text("Editar mensaje") },
                                text = {
                                    OutlinedTextField(
                                        value = nuevoContenido,
                                        onValueChange = { nuevoContenido = it },
                                        label = { Text("Nuevo contenido") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        scope.launch {
                                            supabaseClient
                                                .from("mensaje")
                                                .update({ set("content", nuevoContenido.trim()) }) {
                                                    filter { eq("id", mensaje.id) }
                                                }
                                            showEditDialog = false
                                        }
                                    }) {
                                        Text("Guardar")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showEditDialog = false }) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensajeNuevo,
                    onValueChange = { mensajeNuevo = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") }
                )
                BotonEnviarMensaje {
                    if (mensajeNuevo.isNotBlank()) {
                        scope.launch {
                            val nuevo = Mensaje(
                                content = mensajeNuevo.trim(),
                                idusuario = currentUserId,
                                idconversacion = chatId
                            )
                            supabaseClient.from("mensaje").insert(nuevo)
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }
            }
        }
    }
}
