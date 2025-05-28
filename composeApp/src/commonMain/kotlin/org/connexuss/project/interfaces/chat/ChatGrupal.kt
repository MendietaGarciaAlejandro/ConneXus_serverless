package org.connexuss.project.interfaces.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.encriptacion.EncriptacionSimetricaChats
import org.connexuss.project.encriptacion.desencriptaTexto
import org.connexuss.project.encriptacion.encriptarTexto
import org.connexuss.project.encriptacion.toHex
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.navegacion.TopBarGrupo
import org.connexuss.project.misc.ChatEnviarImagen
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.esAndroid
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseSecretosRepo
import org.connexuss.project.supabase.subscribeTableAsFlow
import org.connexuss.project.supabase.supabaseClient
import org.connexuss.project.usuario.Usuario
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Muestra el chat grupal.
 *
 * Busca la conversación grupal por su identificador y muestra los mensajes compartidos.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación grupal.
 * @param imagenesPerfil Lista de imágenes de perfil de los participantes (opcional).
 */
@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
@Composable
fun mostrarChatGrupo(
    navController: NavHostController,
    chatId: String?,
    imagenesPerfil: List<Imagen> // (No se usa de momento)
) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    var todosUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    val secretoRepositorio = remember { SupabaseSecretosRepo() }
    val escHelper = remember { EncriptacionSimetricaChats() }

    var claveLista by remember { mutableStateOf(false) }

//    val supabaseClient = remember {
//        instanciaSupabaseClient(
//            tieneStorage = true,
//            tieneAuth = true,
//            tieneRealtime = true,
//            tienePostgrest = true
//        )
//    }

    val scope = rememberCoroutineScope()

    var chatNombre by remember { mutableStateOf("Grupo") }
    var chatNombreDesencriptado by remember { mutableStateOf<String>("") }
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

        try {
            val secretoRpc = secretoRepositorio.recuperarSecretoSimpleRpc(chatId)
                ?: throw IllegalStateException("Secreto no disponible")
            // Decodificar clave RAW
            val keyBytes = Base64.decode(secretoRpc.decryptedSecret)
            val aesKey = CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)
            ClaveSimetricaChats.clave = aesKey
            claveLista = true
        } catch (e: Exception) {
            println("Error al recuperar la clave: ${e.message}")
            return@LaunchedEffect
        } finally {
            if (claveLista) {
                println("🔑 Clave lista para usar.")
            } else {
                println("❌ Clave no lista.")
            }
        }

        val repo = SupabaseRepositorioGenerico()
        todosUsuarios = repo.getAll<Usuario>("usuario").first()

        // Cargamos el nombre del grupo
        val conversaciones = repo.getAll<Conversacion>("conversacion").first()
        chatNombre = conversaciones.find { it.id == chatId }?.nombre ?: "Grupo"
    }

    LaunchedEffect(chatNombre) {
        chatNombreDesencriptado = try {
            val cipherBytes = chatNombre.hexToByteArray()
            val plainBytes =
                cipherBytes.let { ClaveSimetricaChats.clave!!.cipher().decrypt(ciphertext = it) }
            plainBytes.decodeToString()
        } catch (e: Exception) {
            println("Error al desencriptar el nombre del grupo: ${e.message}")
            "Error"
        }
    }

    if (chatId == null) {
        ChatLoading(Modifier.fillMaxSize())
        return
    }

    Scaffold(
        topBar = {
            TopBarGrupo(
                title = chatNombreDesencriptado,
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true,
                onUsuariosClick = {
                    navController.navigate("mostrarParticipantesGrupo/$chatId")
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
                    val senderAlias = todosUsuarios
                        .find { it.getIdUnicoMio() == mensaje.idusuario }
                        ?.getAliasPrivadoMio() ?: "Usuario"

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
                        MensajeCard(
                            mensaje = mensaje,
                            esMio = esMio,
                            senderAlias = senderAlias
                        )

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
                                            val textoMensajeBorrado = escHelper.borrarMensaje(
                                                mensajeId = mensaje.id,
                                                clave = ClaveSimetricaChats.clave ?: throw IllegalStateException("Clave no lista")
                                            )
                                            supabaseClient
                                                .from("mensaje")
                                                .update({ set("content", textoMensajeBorrado) }) {
                                                    filter { eq("id", mensaje.id) }
                                                }
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }

                        if (showEditDialog) {
                            scope.launch {
                                nuevoContenido = desencriptaTexto(nuevoContenido, ClaveSimetricaChats.clave ?: throw IllegalStateException("Clave no lista"))
                            }
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
                                            val nuevoContenidoEncriptado = ClaveSimetricaChats.clave?.let {
                                                encriptarTexto(nuevoContenido,
                                                    it
                                                )
                                            }
                                            supabaseClient
                                                .from("mensaje")
                                                .update({ set("content", nuevoContenidoEncriptado) }) {
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
                            val nuevoMensaje = escHelper.crearMensajeSinPadding(
                                contenidoPlain = mensajeNuevo,
                                idConversacion = chatId,
                                idUsuario = currentUserId,
                            )
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }

                if (esAndroid()) {
                    chatId?.let {
                        ChatEnviarImagen(
                            chatId = it,
                            currentUserId = currentUserId
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun mostrarParticipantesGrupo(
    navController: NavHostController,
    chatId: String
) {
    val currentUserId = UsuarioPrincipal?.idUnico ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    var participantes by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatId) {
        try {
            val relaciones = repo.getAll<ConversacionesUsuario>("conversaciones_usuario").first()
            val ids = relaciones
                .filter { it.idconversacion == chatId }
                .map { it.idusuario }

            val todosUsuarios = repo.getAll<Usuario>("usuario").first()
            participantes = todosUsuarios.filter { it.idUnico in ids }
        } catch (e: Exception) {
            println("❌ Error cargando participantes: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Participantes del grupo",
                navController = navController,
                showBackButton = true,
                irParaAtras = true
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(participantes) { usuario ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("mostrarPerfilUsuario/${usuario.idUnico}")
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Nombre: ${usuario.getNombreCompletoMio()}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Alias: ${usuario.getAliasMio()}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
