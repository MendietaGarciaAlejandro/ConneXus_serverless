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

interface ISupabaseTemasRepositorio {
    fun getTemas(): Flow<List<Tema>>
    fun getTemaPorId(idTema: String): Flow<Tema?>
    fun getTemasPorIdBis(idTema: String): Flow<Tema?>
    suspend fun addTema(tema: Tema)
    suspend fun updateTema(tema: Tema)
    suspend fun deleteTema(tema: Tema)
}

class SupabaseTemasRepositorio : ISupabaseTemasRepositorio {

    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://riydmqawtpwmulqlbbjq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJpeWRtcWF3dHB3bXVscWxiYmpxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM2MjIyNTksImV4cCI6MjA1OTE5ODI1OX0.ShUPbRe_6yvIT27o5S7JE8h3ErIJJo-icrdQD1ugl8o",
    ) {
        install(Storage)
        //install(Auth)
        install(Realtime)
        install(Postgrest)
    }
    private val nombreTabla = "temas"

    // Implementación de los métodos de la interfaz
    override fun getTemas() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Tema>()
        emit(response)
    }

    override fun getTemaPorId(idTema: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$idTema"
        val response = supabaseClient
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<Tema>()
        emit(response)
    }

    override fun getTemasPorIdBis(idTema: String) = flow {
        // Recojo todos los temas
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Tema>()
        // Filtro los temas por idTema
        val filteredResponse = response.find { it.idTema == idTema }
        emit(filteredResponse)
    }

    override suspend fun addTema(tema: Tema) {
        val response = supabaseClient
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
            "idUsuario" to tema.idUsuario,
            "nombre" to tema.nombre,
            "hilos" to tema.hilos
        )
        try {
            supabaseClient
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
            supabaseClient
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