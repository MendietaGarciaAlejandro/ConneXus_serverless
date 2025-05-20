package org.connexuss.project.interfaces.notificaciones

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.misc.Supabase

class HiloViewModel : ViewModel() {
    private var _newPostsCount = 0
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
