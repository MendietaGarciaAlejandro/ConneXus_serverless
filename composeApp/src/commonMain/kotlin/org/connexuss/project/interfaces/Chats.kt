package org.connexuss.project.interfaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.encriptacion.EncriptacionSimetricaChats
import org.connexuss.project.encriptacion.toHex
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseSecretosRepo
import org.connexuss.project.supabase.instanciaSupabaseClient
import org.connexuss.project.supabase.subscribeTableAsFlow
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.resources.DrawableResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Muestra el chat entre dos usuarios.
 *
 * Busca la conversación por su identificador y muestra los mensajes intercambiados.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación.
 */
@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class, DelicateCoroutinesApi::class)
@Composable
fun mostrarChat(navController: NavHostController, chatId: String?) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
//    val supabaseClient = remember {
//        instanciaSupabaseClient(
//            tieneStorage = true,
//            tieneAuth = true,
//            tieneRealtime = true,
//            tienePostgrest = true
//        )
//    }
    val scope = rememberCoroutineScope()

    var participantes by remember { mutableStateOf<List<String>>(emptyList()) }
    var otroUsuarioNombre by remember { mutableStateOf<String?>(null) }
    var otroUsuarioImagen by remember { mutableStateOf<DrawableResource?>(null) }
    var mensajeNuevo by remember { mutableStateOf("") }

    val secretoRepositorio = remember { SupabaseSecretosRepo() }

    var claveLista by remember { mutableStateOf(false) }

    val todosLosMensajes by Supabase.client
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
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
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

                    MensajeCard(
                        mensaje = mensaje,
                        esMio = esMio
                    )
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
                IconButton(onClick = {
                    if (mensajeNuevo.isNotBlank()) {
                        scope.launch {
                            val key = ClaveSimetricaChats.clave ?: throw IllegalStateException("Clave no lista")
                            // Ciframos con nonce incluido
                            val encryptedFull = key.cipher().encrypt(mensajeNuevo.encodeToByteArray())
                            val contenidoHex = encryptedFull.toHex()

                            val nuevo = Mensaje(
                                content = contenidoHex,
                                idusuario = currentUserId,
                                idconversacion = chatId
                            )
                            Supabase.client.from("mensaje").insert(nuevo)
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}

/**
 * Muestra el chat grupal.
 *
 * Busca la conversación grupal por su identificador y muestra los mensajes compartidos.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación grupal.
 * @param imagenesPerfil Lista de imágenes de perfil de los participantes.
 */
@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
@Composable
fun mostrarChatGrupo(
    navController: NavHostController,
    chatId: String?,
    imagenesPerfil: List<Imagen>   // (No se usa de momento, opcional en el futuro)
) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    var todosUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    val secretoRepositorio = remember { SupabaseSecretosRepo() }

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

    var chatNombre by remember { mutableStateOf<String>("") }
    var chatNombreDesencriptado by remember { mutableStateOf<String>("") }
    var mensajeNuevo by remember { mutableStateOf("") }

    val todosLosMensajes by Supabase.client
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
            val plainBytes  = cipherBytes.let { ClaveSimetricaChats.clave!!.cipher().decrypt(ciphertext = it) }
            plainBytes.decodeToString()
        } catch (e: Exception) {
            println("Error al desencriptar el nombre del grupo: ${e.message}")
            "Grupo"
        }
    }

    if (chatId == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
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
            // Lista de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(mensajes.sortedBy { it.fechaMensaje }) { mensaje ->
                    val esMio = mensaje.idusuario == currentUserId
                    val senderAlias = todosUsuarios
                        .find { it.getIdUnicoMio() == mensaje.idusuario }
                        ?.getAliasPrivadoMio() ?: "Usuario"

                    MensajeCard(
                        mensaje = mensaje,
                        esMio = esMio,
                        senderAlias = senderAlias
                    )
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
                IconButton(onClick = {
                    if (mensajeNuevo.isNotBlank()) {
                        scope.launch {
                            val key = ClaveSimetricaChats.clave ?: throw IllegalStateException("Clave no lista")
                            // Ciframos con nonce incluido
                            val encryptedFull = key.cipher().encrypt(mensajeNuevo.encodeToByteArray())
                            val contenidoHex = encryptedFull.toHex()

                            val nuevo = Mensaje(
                                content = contenidoHex,
                                idusuario = currentUserId,
                                idconversacion = chatId
                            )
                            Supabase.client.from("mensaje").insert(nuevo)
                            mensajeNuevo = ""
                            println("📤 Mensaje enviado en realtime.")
                        }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MensajeCard(
    mensaje: Mensaje,
    esMio: Boolean,
    senderAlias: String? = null
) {

    var nombrePlano by remember { mutableStateOf("(cargando...)") }

    LaunchedEffect(ClaveSimetricaChats.clave, mensaje.content) {
        if (ClaveSimetricaChats.clave != null) {
            val cipherBytes = mensaje.content.hexToByteArray()
            val plainBytes  = cipherBytes.let { ClaveSimetricaChats.clave!!.cipher().decrypt(ciphertext = it) }
            nombrePlano    = plainBytes.decodeToString()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (esMio) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            if (!esMio) {
                if (senderAlias != null) {
                    Text(
                        text = senderAlias,
                        style = MaterialTheme.typography.caption,
                        color = Color.DarkGray
                    )
                }
            }
            Text(nombrePlano)
        }
    }
}