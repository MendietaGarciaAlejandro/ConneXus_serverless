package org.connexuss.project.interfaces.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.encriptacion.EncriptacionSimetricaChats
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.navegacion.MiBottomBar
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseSecretosRepo
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UsuarioBloqueado
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// --- elemento chat ---
//Muestra el id del usuarioPrincipal ya que no esta incluido en la lista de usuarios precreados
@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
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

    var nombrePlano by remember { mutableStateOf("(cargando…)") }

    var claveChat: AES.GCM.Key?

    var ultimoMensajeDesencriptado by remember { mutableStateOf("") }

    val encHelper = remember { EncriptacionSimetricaChats() }

    val scope = rememberCoroutineScope()

    val secretoRepositorio = remember { SupabaseSecretosRepo() }

    if (ultimoMensaje != null) {
        LaunchedEffect(ultimoMensaje.content) {
            val secretoRpc = secretoRepositorio.recuperarSecretoSimpleRpc(conversacion.id)
                ?: throw IllegalStateException("Secreto no disponible")
            // Decodificar clave RAW
            val keyBytes = Base64.decode(secretoRpc.decryptedSecret)
            claveChat = CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)

            if (claveChat != null) {
                val cipherBytes = ultimoMensaje.content.hexToByteArray()
                val plainBytes  = cipherBytes.let { claveChat!!.cipher().decrypt(ciphertext = it) }
                ultimoMensajeDesencriptado    = plainBytes.decodeToString()
            }
        }
    }

    scope.launch {
        encHelper.leerConversacion(
            converId = conversacion.id,
            secretsRpcRepo = secretoRepositorio,
        ).let { nombre ->
            if (nombre != null) {
                nombrePlano = nombre
            }
        }
    }

    println("👥 Participantes en la conversación ${conversacion.id}:")
    participantes.forEach {
        println(" - ${it.getNombreCompletoMio()} (id: ${it.getIdUnicoMio()})")
    }
    println("🧍 Usuario actual: $currentUserId")

    val otroUsuario = participantes.firstOrNull { it.getIdUnicoMio() != currentUserId }
    val estaBloqueado = otroUsuario?.getIdUnicoMio() in bloqueados

    val displayName = if (esGrupo) {
        nombrePlano
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

            if (ultimoMensaje != null) {
                Text(
                    text = ultimoMensajeDesencriptado,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (estaBloqueado) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

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
