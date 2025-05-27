package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.misc.Supabase

interface ISupabaseTemasRepositorio {
    fun getTemas(): Flow<List<Tema>>
    fun getTemaPorId(idTema: String): Flow<Tema?>
    fun getTemasPorIdBis(idTema: String): Flow<Tema?>
    suspend fun addTema(tema: Tema)
    suspend fun updateTema(tema: Tema)
    suspend fun deleteTema(tema: Tema)
}

class SupabaseTemasRepositorio : ISupabaseTemasRepositorio {

    //private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "tema"

    // Implementación de los métodos de la interfaz
    override fun getTemas() = flow {
        val response = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Tema>()
        emit(response)
    }

    override fun getTemaPorId(idTema: String) = flow {
        val tableWithFilter = "$nombreTabla?idtema=eq.$idTema"
        val response = Supabase.client
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<Tema>()
        emit(response)
    }

    override fun getTemasPorIdBis(idTema: String) = flow {
        // Recojo todos los temas
        val response = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Tema>()
        // Filtro los temas por idTema
        val filteredResponse = response.find { it.idTema == idTema }
        emit(filteredResponse)
    }

    override suspend fun addTema(tema: Tema) {
        val response = Supabase.client
            .from(nombreTabla)
            .insert(tema)
            .decodeSingleOrNull<Tema>()
        if (response == null) {
            throw Exception("Error al agregar el tema")
        } else {
            println("Tema agregado: $response")
        }
    }

    override suspend fun updateTema(tema: Tema) {
        val updateData = mapOf(
            "idTema" to tema.idTema,
            "nombre" to tema.nombre,
        )
        try {
            Supabase.client
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("idTema", tema.idTema)
                    }
                    select()
                }
                .decodeList<Post>()
                .run {
                    if (this.isEmpty()) {
                        println("Tema actualizado: $tema")
                    } else {
                        println("Error al actualizar el tema")
                    }
                }
        } catch (e: Exception) {
            println("Error al actualizar el tema: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteTema(tema: Tema) {
        try {
            Supabase.client
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("idTema", tema.idTema)
                    }
                }
                .decodeList<Tema>()
                .run {
                    if (this.isEmpty()) {
                        println("Tema eliminado: $tema")
                    } else {
                        println("Error al eliminar el tema")
                    }
                }
        } catch (e: Exception) {
            println("Error al eliminar el tema: ${e.message}")
            throw e
        }
    }
}