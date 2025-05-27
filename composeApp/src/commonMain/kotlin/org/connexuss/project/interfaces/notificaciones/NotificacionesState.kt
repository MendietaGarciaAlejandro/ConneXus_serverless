package org.connexuss.project.interfaces.notificaciones

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal

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
                    if (change.idFirmante != UsuarioPrincipal?.idUnico) {
                        onNewPost()
                    }
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
