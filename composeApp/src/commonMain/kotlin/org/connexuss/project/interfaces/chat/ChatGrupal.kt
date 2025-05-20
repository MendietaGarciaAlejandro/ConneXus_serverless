package org.connexuss.project.interfaces.chat

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.from
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.interfaces.navegacion.TopBarGrupo
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.instanciaSupabaseClient
import org.connexuss.project.supabase.subscribeTableAsFlow
import org.connexuss.project.usuario.Usuario

/**
 * Muestra el chat grupal.
 *
 * Busca la conversación grupal por su identificador y muestra los mensajes compartidos.
 * @param navController Controlador de navegación.
 * @param chatId Identificador de la conversación grupal.
 * @param imagenesPerfil Lista de imágenes de perfil de los participantes (opcional).
 */
@Composable
fun mostrarChatGrupo(
    navController: NavHostController,
    chatId: String?,
    imagenesPerfil: List<Imagen> = emptyList()  // Opcional para uso futuro
) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    var todosUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    val supabaseClient = remember {
        instanciaSupabaseClient(
            tieneStorage = true,
            tieneAuth = true,
            tieneRealtime = true,
            tienePostgrest = true
        )
    }
    val scope = rememberCoroutineScope()

    var chatNombre by remember { mutableStateOf<String>("") }
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
        todosUsuarios = repo.getAll<Usuario>("usuario").first()

        // Cargamos el nombre del grupo
        val conversaciones = repo.getAll<Conversacion>("conversacion").first()
        chatNombre = conversaciones.find { it.id == chatId }?.nombre ?: "Grupo"
    }

    if (chatId == null) {
        ChatLoading(Modifier.fillMaxSize())
        return
    }

    Scaffold(
        topBar = {
            TopBarGrupo(
                title = chatNombre,
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
                                Text(
                                    text = senderAlias,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }
                            Text(text = mensaje.content)
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
