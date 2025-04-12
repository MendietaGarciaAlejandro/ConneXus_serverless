package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.Mensaje

interface ISupabaseMensajesRepositorio {
    fun getMensajes(): Flow<List<Mensaje>>
    fun getMensajePorId(id: String): Flow<Mensaje?>
    fun getMensajePorIdBis(id: String): Flow<Mensaje?>
    suspend fun addMensaje(mensaje: Mensaje)
    suspend fun updateMensaje(mensaje: Mensaje)
    suspend fun deleteMensaje(mensaje: Mensaje)
}

class SupabaseMensajesRepositorio : ISupabaseMensajesRepositorio {

    private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "mensajes"

    override fun getMensajes() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Mensaje>()
        emit(response)
    }

    override fun getMensajePorId(id: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$id"
        val response = supabaseClient
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Mensaje>()
        emit(response)
    }

    override fun getMensajePorIdBis(id: String) = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Mensaje>()
        val filteredResponse = response.find { it.id == id }
        emit(filteredResponse)
    }

    override suspend fun addMensaje(mensaje: Mensaje) {
        val response = supabaseClient
            .from(nombreTabla)
            .insert(mensaje)
            .decodeSingleOrNull<Supausuario>()
        if (response == null) {
            throw Exception("Error al agregar el mensaje")
        } else {
            println("Mensaje agregado: $response")
        }
    }

    override suspend fun updateMensaje(mensaje: Mensaje) {
        val updateData = mapOf(
            "id" to mensaje.id,
            "senderId" to mensaje.senderId,
            "receiverId" to mensaje.receiverId,
            "content" to mensaje.content,
            "fechaMensaje" to mensaje.fechaMensaje
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("id", mensaje.id)
                    }
                    select()
                }
                .decodeList<Conversacion>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al actualizar el mensaje")
                    } else {
                        println("Mensaje actualizado: $this")
                    }
                }
        } catch (e: Exception) {
            throw Exception("Error actualizando mensaje: ${e.message}")
        }
    }

    override suspend fun deleteMensaje(mensaje: Mensaje) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("id", mensaje.id)
                    }
                }
                .decodeList<Conversacion>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al eliminar el mensaje")
                    } else {
                        println("Mensaje eliminado: $this")
                    }
                }
        } catch (e: Exception) {
            throw Exception("Error eliminando mensaje: ${e.message}")
        }
    }
}