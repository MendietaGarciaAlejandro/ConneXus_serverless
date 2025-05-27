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
import org.connexuss.project.misc.Supabase

interface IConversacionesRepositorio {
    fun getConversaciones(): Flow<List<Conversacion>>
    fun getConversacionPorId(id: String): Flow<Conversacion?>
    fun getConversacionPorIdBis(id: String): Flow<Conversacion?>
    suspend fun addConversacion(conversacion: Conversacion)
    suspend fun updateConversacion(conversacion: Conversacion)
    suspend fun deleteConversacion(conversacion: Conversacion)
}

class SupabaseConversacionesRepositorio : IConversacionesRepositorio {

    //private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "conversacion"

    override fun getConversaciones() = flow {
        val response = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Conversacion>()
        emit(response)
    }

    override fun getConversacionPorId(id: String) = flow {
        // Construye la URL con el parámetro de consulta para filtrar por id
        val tableWithFilter = "$nombreTabla?id=eq.$id"
        val response = Supabase.client
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Conversacion>()
        emit(response)
    }

    override fun getConversacionPorIdBis(id: String) = flow {
        val convers = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Conversacion>()
        val conversacion = convers.find { it.id == id }
        emit(conversacion)
    }

    override suspend fun addConversacion(conversacion: Conversacion) {
        // Implementación para agregar un usuario a Supabase
        val response = Supabase.client
            .from(nombreTabla)
            .insert(conversacion)
            .decodeSingleOrNull<Conversacion>()
        if (response == null) {
            throw Exception("Error al agregar la conversación")
        } else {
            println("Conversación agregada: $response")
        }
    }

    override suspend fun updateConversacion(conversacion: Conversacion) {
        val updateData = mapOf(
            "id" to conversacion.id,
            "nombre" to conversacion.nombre
        )
        try {
            Supabase.client
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("id", conversacion.id)
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
            throw Exception("Error actualizando coversacion: ${e.message}")
        }
    }

    override suspend fun deleteConversacion(conversacion: Conversacion) {
        try {
            Supabase.client
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
            throw Exception("Error eliminando conversación: ${e.message}")
        }
    }
}