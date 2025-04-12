package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario

interface ISupabaseConversacionesUsuarioRepositorio {
    fun getConversacionesUsuario(): Flow<List<ConversacionesUsuario>>
    fun getConversacionPorId(idconversacionUsuario: String): Flow<ConversacionesUsuario?>
    fun getConversacionPorIdBis(idconversacionUsuario: String): Flow<ConversacionesUsuario?>
    suspend fun addConversacion(conversacionUsuario: ConversacionesUsuario)
    suspend fun updateConversacion(conversacionUsuario: ConversacionesUsuario)
    suspend fun deleteConversacion(conversacionUsuario: ConversacionesUsuario)
}

class SupabaseConversacionesUsuarioRepositorio : ISupabaseConversacionesUsuarioRepositorio {

    private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "conversacionesusuario"

    override fun getConversacionesUsuario() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<ConversacionesUsuario>()
        emit(response)
    }

    override fun getConversacionPorId(idconversacionUsuario: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$idconversacionUsuario"
        val response = supabaseClient
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<ConversacionesUsuario>()
        emit(response)
    }

    override fun getConversacionPorIdBis(idconversacionUsuario: String) = flow {
        val convers = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<ConversacionesUsuario>()
        val conversacion = convers.find { it.id == idconversacionUsuario }
        emit(conversacion)
    }

    override suspend fun addConversacion(conversacionUsuario: ConversacionesUsuario) {
        val response = supabaseClient
            .from(nombreTabla)
            .insert(conversacionUsuario)
            .decodeSingleOrNull<ConversacionesUsuario>()
        if (response == null) {
            throw Exception("Error al agregar la conversación")
        } else {
            println("Conversación agregada: $response")
        }
    }

    override suspend fun updateConversacion(conversacionUsuario: ConversacionesUsuario) {
        val updateData = mapOf(
            "id" to conversacionUsuario.id,
            "idUser" to conversacionUsuario.idUser,
            "conversaciones" to conversacionUsuario.conversaciones,
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("id", conversacionUsuario.id)
                    }
                    select()
                }
                .decodeList<Conversacion>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al actualizar la conversación")
                    } else {
                        println("Conversación actualizada: $this")
                    }
                }
        } catch (e: Exception) {
            println("Error al actualizar la conversación: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteConversacion(conversacionUsuario: ConversacionesUsuario) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("id", conversacionUsuario.id)
                    }
                }
                .decodeList<Conversacion>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al eliminar la conversación")
                    } else {
                        println("Conversación eliminada: $this")
                    }
                }
        } catch (e: Exception) {
            println("Error al eliminar la conversación: ${e.message}")
            throw e
        }
    }
}