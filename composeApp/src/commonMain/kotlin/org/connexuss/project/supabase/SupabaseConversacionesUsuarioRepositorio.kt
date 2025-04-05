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
    fun getConversacionPorId(idConversacion: String): Flow<ConversacionesUsuario?>
    suspend fun addConversacion(conversacion: ConversacionesUsuario)
    suspend fun updateConversacion(conversacion: ConversacionesUsuario)
    suspend fun deleteConversacion(conversacion: ConversacionesUsuario)
}

class SupabaseConversacionesUsuarioRepositorio : ISupabaseConversacionesUsuarioRepositorio {

    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://riydmqawtpwmulqlbbjq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJpeWRtcWF3dHB3bXVscWxiYmpxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM2MjIyNTksImV4cCI6MjA1OTE5ODI1OX0.ShUPbRe_6yvIT27o5S7JE8h3ErIJJo-icrdQD1ugl8o",
    ) {
        install(Storage)
        //install(Auth)
        install(Realtime)
        install(Postgrest)
    }
    private val nombreTabla = "conversacionesusuario"

    override fun getConversacionesUsuario() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<ConversacionesUsuario>()
        emit(response)
    }

    override fun getConversacionPorId(idConversacion: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$idConversacion"
        val response = supabaseClient
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<ConversacionesUsuario>()
        emit(response)
    }

    override suspend fun addConversacion(conversacion: ConversacionesUsuario) {
        val response = supabaseClient
            .from(nombreTabla)
            .insert(conversacion)
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

    override suspend fun deleteConversacion(conversacion: ConversacionesUsuario) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("id", conversacion.id)
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