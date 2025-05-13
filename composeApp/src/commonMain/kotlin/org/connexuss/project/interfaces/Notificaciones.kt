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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.misc.Supabase

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
                    Icon(Icons.Default.Refresh, contentDescription = "Nuevos posts")
                }
            }
            if (showRefresh) {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }
        }
    )
}
