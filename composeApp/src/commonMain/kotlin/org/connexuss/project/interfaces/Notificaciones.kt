package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.postgrest.query.request.RpcRequestBuilder
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseConversacionesUsuarioRepositorio
import org.connexuss.project.usuario.Usuario

// ---------------------------------Parte del Foro ---------------------------------

var currentHiloId = mutableStateOf("")
var initialCount = mutableStateOf(0)
var newPostsCount = mutableStateOf(0)
val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
val filter: FilterOperation = FilterOperation(
    column = "idhilo",
    operator = FilterOperator.EQ,
    value = currentHiloId.value
)

@OptIn(SupabaseExperimental::class)
val flow: Flow<List<Post>> = Supabase.client
    .from("post")
    .selectAsFlow(
        primaryKey = Post::idPost,
        channelName = "public:post",
        filter = filter
    )
    .onEach { posts ->
        initialCount.value = posts.size
        newPostsCount.value = posts.size - initialCount.value
    }

class HiloViewModel : ViewModel() {
    private var _newPostsCount by mutableStateOf(0)
    var newPostsCount: State<Int> = mutableStateOf(0)

    suspend fun startListeningToNewPosts(currentIdhilo: String) {
        val scope = viewModelScope  // o cualquier CoroutineScope válido

        val channel = Supabase.client.realtime.channel("public:post")

        channel.postgresChangeFlow<PostgresAction.Insert>("public") {
            table = "post"
        }.map { it.decodeRecord<Post>() }
            .onEach { change ->
                if (change.idHilo == currentIdhilo) {
                    onNewPost()
                }
        }.launchIn(scope)

        channel.subscribe()
    }

    private fun onNewPost() {
        _newPostsCount++
    }
    fun resetCounter() {
        _newPostsCount = 0
    }
}

class HiloState(hiloId: String) {
    // Expuesto a la UI
    var newPostsCount: MutableState<Int> = mutableStateOf(0)

    private val scope: CoroutineScope = MainScope()
    private val channel: RealtimeChannel =
        Supabase.client.realtime.channel("public:post")

    private suspend fun startListeningToNewPosts(currentIdhilo: String) {
        val channel = Supabase.client.realtime.channel("public:post")

        channel.postgresChangeFlow<PostgresAction.Insert>("public") {
            table = "post"
        }.map { it.decodeRecord<Post>() }
            .onEach { change ->
                if (change.idHilo == currentIdhilo) {
                    onNewPost()
                }
            }.launchIn(scope)

        channel.subscribe()
    }

    init {
        scope.launch {
            startListeningToNewPosts(
                hiloId.toString()
            )
        }
    }

    /** Llamar para detener la escucha (por ejemplo al desmontar el Composable) */
    suspend fun stop() {
        channel.unsubscribe()
        scope.cancel(
            "HiloState: stop()"
        )
    }

    /** Resetear contador (por ejemplo al pulsar refresh) */
    fun reset() {
        newPostsCount.value = 0
    }

    private fun onNewPost() {
        newPostsCount.value++
    }
    fun resetCounter() {
        newPostsCount = mutableStateOf(0)
    }
}

@Composable
fun HiloTopBar(
    title: String,
    navController: NavHostController?,
    newPostsCount: Int,
    onRefresh: () -> Unit,
    showRefresh: Boolean = true,
    startRoute: String
) {
    TopAppBar(
        title = { Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = {
                navController?.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        },
        actions = {
            if (newPostsCount > 0) {
                BadgedBox(
                    badge = { Badge { Text("$newPostsCount") } },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            }
//            if (showRefresh) {
//                IconButton(onClick = onRefresh) {
//                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
//                }
//            }
        }
    )
}

// ---------------------------------Parte del Chat ---------------------------------

data class ChatSummary(
    val conversacion: Conversacion,
    val participantes: List<Usuario>,
    val ultimoMensaje: Mensaje?,
    val unreadCount: Int
)

class ChatState(conversacionId: String, userId: String) {
    val mensajes = mutableStateOf<List<Mensaje>>(emptyList())
    private var newMessagesCount = mutableStateOf(0)

    private val scope = MainScope()
    private val channel = Supabase.client.realtime.channel("public:mensaje")

    init {
        fetchMessages(conversacionId)  // carga inicial y resetea badge

        scope.launch {
            startListeningToNewMessages(conversacionId, userId)
        }
    }

    private suspend fun startListeningToNewMessages(conversacionId: String, userId: String) {
        var filtroConver = channel.postgresChangeFlow<PostgresAction.Insert>("public") {
            table = "mensaje"
            filter(
                FilterOperation(
                    column = "idconversacion",
                    operator = FilterOperator.EQ,
                    value = conversacionId
                )
            )
            filter(
                FilterOperation(
                    column = "idusuario",
                    operator = FilterOperator.NEQ,
                    value = userId
                )
            )
        }/*.filter {
            it.decodeRecord<Mensaje>().idconversacion == conversacionId
        }*/.map { action -> action.decodeRecord<Mensaje>() }
            .onEach { nuevoMensaje ->
                val currentUserId = UsuarioPrincipal?.getIdUnicoMio()
                println("✔️ Mensaje de otro usuario, lo contamos")
                if (nuevoMensaje.idusuario != currentUserId) {
                    println("✔️ Mensaje de otro usuario, lo contamos")
                    // Añádelo a la lista
                    mensajes.value = mensajes.value + nuevoMensaje
                    // Incrementa el badge
                    newMessagesCount.value += 1
                } else {
                    println("🚫 Mensaje propio, lo ignoro")
                }
            }
            .launchIn(scope)
        channel.subscribe()
    }

    private fun fetchMessages(conversacionId: String) = scope.launch {
        val resp = Supabase.client
            .from("mensaje")
            .select {
                filter {
                    eq("idconversacion", conversacionId)
                }
            }

        // Decodifica la respuesta
        val jsonString: String = resp.data
        try {
            val listaDeMensajes: List<Mensaje> = Json.decodeFromString(jsonString)
            mensajes.value = listaDeMensajes
            newMessagesCount.value = 0
        } catch (_: Exception) {
        }
//        resp.data.let {
//            if (it != null) {
//                mensajes.value = it
//                newMessagesCount.value = 0 // Resetea el badge
//            } else {
//                // Maneja el error
//            }
//        }
    }

    companion object {
        suspend fun fetchChatSummaries(userId: String): List<ChatSummary> {

            val supa = Supabase.client

            suspend fun fetchConversacion(conversacionId: String): Conversacion =
                supa.postgrest
                    .from("conversacion")
                    .select {
                        filter {
                            eq("id", conversacionId)
                        }
                    }.decodeSingle()

            suspend fun fetchParticipantes(conversacionId: String): List<Usuario> =
                supa.postgrest
                    .from("conversaciones_usuario")
                    .select {
                        filter {
                            eq("idconversacion", conversacionId)
                        }
                    }
                    .decodeList<ConversacionesUsuario>()
                    .mapNotNull { rel ->
                        supa.postgrest
                            .from("usuario")
                            .select {
                                filter {
                                    eq("idunico", rel.idusuario)
                                }
                            }.decodeSingle()
                    }

            suspend fun fetchUltimoMensaje(conversacionId: String): Mensaje? =
                supa.postgrest
                    .from("mensaje")
                    .select {
                        filter {
                            eq("idconversacion", conversacionId)
                        }
                        order("fechamensaje", order = Order.DESCENDING)
                        limit(1)
                    }.decodeSingleOrNull()

            // 1. Carga las relaciones con last_read
            val rels: List<ConversacionesUsuario> = supa
                .from("conversaciones_usuario")
                .select {
                    filter {
                        eq("idusuario", userId)
                    }
                }
                .decodeList<ConversacionesUsuario>()

            // 2. Para cada relación calcula el count de mensajes posteriores a last_read
            return rels.map { rel ->
                val params = buildJsonObject {
                    put("p_conversacion", JsonPrimitive(rel.idconversacion))
                    // Si lastRead es Instant o LocalDateTime, pásalo como String
                    put("p_last_read", JsonPrimitive(rel.lastRead.toString()))
                }

                val countResp: Int = supa
                    .postgrest
                    .rpc("count_unread", params)
                    .decodeAs<Int>()

                // coge último mensaje igual que ahora
                ChatSummary(
                    conversacion = fetchConversacion(rel.idconversacion),
                    participantes = fetchParticipantes(rel.idconversacion),
                    ultimoMensaje = fetchUltimoMensaje(rel.idconversacion),
                    unreadCount = countResp
                )
            }
        }

        suspend fun markAsRead(userId: String, conversacionId: String) {
            try {
                Supabase.client
                    .from("conversaciones_usuario")
                    .update(mapOf("last_read" to Clock.System.now().toString())) {
                        // Aquí va el DSL con tus filtros .eq()
                        filter {
                            eq("idusuario", userId)
                            eq("idconversacion", conversacionId)
                        }
                        select()  // opcional: para devolver la fila actualizada
                    }
                    .decodeList<ConversacionesUsuario>() // o el DTO que necesites
                    .also { updated ->
                        println("ConversationsUsuario actualizado: $updated")
                    }
            } catch (e: Exception) {
                println("Error al marcar como leído: ${e.message}")
                throw e
            }
        }
    }

    // Llama a esto justo cuando el usuario abra la conversación
    suspend fun onConversationOpened( userId: String, conversacionId: String) {
        try {
            // 1) Marca como leído en la base
            markAsRead(userId, conversacionId)

            // 2) Obtén el nuevo count (debería ser 0 tras el update)
            val params = buildJsonObject {
                put("p_conversacion", JsonPrimitive(conversacionId))
                put("p_last_read", JsonPrimitive(Clock.System.now().toString()))
            }
            val newCount: Int = Supabase.client
                .postgrest
                .rpc(
                    "count_unread",
                    params
                ) {
                    count(Count.EXACT)
                }
                .decodeAs<Int>()

            // 3) Actualiza tu estado local
            newMessagesCount.value = newCount
        } catch (e: Exception) {
            println("Error al marcar como leído: ${e.message}")
        }
    }

    fun stop() {
        scope.launch {
            channel.unsubscribe()
        }
        scope.cancel()
    }
}

class ChatsViewModel : ViewModel() {

    private val currentUserId: String
        get() = UsuarioPrincipal?.getIdUnicoMio()
            ?: throw IllegalStateException("Usuario no logueado")

    private val _summaries = MutableStateFlow<List<ChatSummary>>(emptyList())
    val summaries: StateFlow<List<ChatSummary>> = _summaries.asStateFlow()

    init {
        viewModelScope.launch { loadSummaries() }
    }

    private suspend fun loadSummaries() {
        val list = ChatState.fetchChatSummaries(currentUserId)
        _summaries.value = list
    }

    fun markAsRead(conversacionId: String) {
        viewModelScope.launch {
            // Actualiza last_read en Supabase
            ChatState.markAsRead(currentUserId, conversacionId)
            // Refresca sólo ese resumen:
            _summaries.update { oldList ->
                oldList.map {
                    if (it.conversacion.id == conversacionId) it.copy(unreadCount = 0)
                    else it
                }
            }
        }
    }
}
