package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post

interface ISupabaseHiloRepositorio {
    fun getHilos(): Flow<List<Hilo>>
    fun getHiloPorId(idHilo: String): Flow<Hilo?>
    fun getHiloPorIdBis(idHilo: String): Flow<Hilo?>
    suspend fun addHilo(hilo: Hilo)
    suspend fun updateHilo(hilo: Hilo)
    suspend fun deleteHilo(hilo: Hilo)
}

class SupabaseHiloRepositorio : ISupabaseHiloRepositorio {

    private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "hilos"

    override fun getHilos() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Hilo>()
        emit(response)
    }

    override fun getHiloPorId(idHilo: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$idHilo"
        val response = supabaseClient
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<Hilo>()
        emit(response)
    }

    override fun getHiloPorIdBis(idHilo: String) = flow {
        val hilos = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Hilo>()
        val hilo = hilos.find { it.idHilo == idHilo }
        emit(hilo)
    }

    override suspend fun addHilo(hilo: Hilo) {
        val response = supabaseClient
            .from(nombreTabla)
            .insert(hilo)
            .decodeSingleOrNull<Hilo>()
        if (response == null) {
            throw Exception("Error al agregar el hilo")
        } else {
            println("Hilo agregado: $response")
        }
    }

    override suspend fun updateHilo(hilo: Hilo) {
        val updateData = mapOf(
            "idHilo" to hilo.idHilo,
            "idForeros" to hilo.idForeros,
            "posts" to hilo.posts,
            "nombre" to hilo.nombre
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("id", hilo.idHilo)
                    }
                    select()
                }
                .decodeList<Hilo>()
                .run {
                    if (isNotEmpty()) {
                        println("Hilo actualizado: $this")
                    } else {
                        println("No se encontró el hilo para actualizar.")
                    }
                }
        } catch (e: Exception) {
            println("Error al actualizar el hilo: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteHilo(hilo: Hilo) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("idHilo", hilo.idHilo)
                    }
                }
                .decodeList<Hilo>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al eliminar el hilo")
                    } else {
                        println("Hilo eliminado: $this")
                    }
                }
        } catch (e: Exception) {
            println("Error al eliminar el hilo: ${e.message}")
            throw e
        }
    }
}