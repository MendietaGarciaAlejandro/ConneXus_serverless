package org.connexuss.project.interfaces.notificaciones

import androidx.compose.runtime.mutableStateOf
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.misc.Supabase

class ChatState(conversacionId: String) {
    val mensajes = mutableStateOf<List<Mensaje>>(emptyList())
    val newMessagesCount = mutableStateOf(0)

    private val scope = MainScope()
    private val channel = Supabase.client.realtime.channel("public:mensaje")

    init {
        fetchMessages(conversacionId)  // carga inicial y resetea badge

        scope.launch {
            startListeningToNewMessages(conversacionId)
        }
    }

    private suspend fun startListeningToNewMessages(conversacionId: String) {
        var filtroConver = channel.postgresChangeFlow<PostgresAction.Insert>("public") {
            table = "mensaje"
        }.filter {
            it.decodeRecord<Mensaje>().idconversacion == conversacionId
        }.map { action -> action.decodeRecord<Mensaje>() }
            .onEach { nuevoMensaje ->
                // Añádelo a la lista
                mensajes.value = mensajes.value + nuevoMensaje
                // Incrementa el badge
                newMessagesCount.value += 1
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
    }

    fun stop() {
        scope.launch {
            channel.unsubscribe()
        }
        scope.cancel()
    }
}
