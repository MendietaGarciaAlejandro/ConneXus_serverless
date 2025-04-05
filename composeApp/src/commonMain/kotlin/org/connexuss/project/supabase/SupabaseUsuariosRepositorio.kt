package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.usuario.Usuario

// Interfaz para simular una aplicacion CRUD que comunica con Supabase
interface ISupabaseUsuariosRepositorio {
    suspend fun getUsuarios(): Flow<List<Usuario>>
    suspend fun getUsuarioPorId(id: String): Flow<Usuario?>
    suspend fun getUsuarioPorEmail(email: String): Flow<Usuario?>
    suspend fun addUsuario(usuario: Usuario)
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(usuario: Usuario)
}

class SupabaseUsuariosRepositorio : ISupabaseUsuariosRepositorio {

    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://riydmqawtpwmulqlbbjq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJpeWRtcWF3dHB3bXVscWxiYmpxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM2MjIyNTksImV4cCI6MjA1OTE5ODI1OX0.ShUPbRe_6yvIT27o5S7JE8h3ErIJJo-icrdQD1ugl8o",
    ) {
        install(Storage)
        //install(Auth)
        install(Realtime)
        install(Postgrest)
    }
    private val nombreTabla = "usuarios"

    override suspend fun getUsuarios() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        emit(response)
    }

    override suspend fun getUsuarioPorId(id: String) = flow {
        // Construye la URL con el parámetro de consulta para filtrar por id
        val tableWithFilter = "$nombreTabla?idUnico=eq.$id"
        val response = supabaseClient
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Usuario>()
        emit(response)
    }
    override suspend fun getUsuarioPorEmail(email: String) = flow {
        // Construye la URL con el parámetro de consulta para filtrar por email
        val tableWithFilter = "$nombreTabla?correo=eq.$email"
        val response = supabaseClient
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Usuario>()
        emit(response)
    }

    override suspend fun addUsuario(usuario: Usuario)  {
        // Implementación para agregar un usuario a Supabase
        val response = supabaseClient
            .from(nombreTabla)
            .insert(usuario)
            .decodeSingleOrNull<Usuario>()
        if (response != null) {
            println("Usuario agregado: $response")
        } else {
            throw Exception("Error al agregar el usuario")
        }
    }

    override suspend fun updateUsuario(usuario: Usuario) {
        val updateData = mapOf(
            "nombre" to usuario.getNombreCompleto(),
            "correo" to usuario.getCorreo(),
            "contrasennia" to usuario.getContrasennia(),
            "idUnico" to usuario.getIdUnico()
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("idUnico", usuario.getIdUnico())
                    }
                    select()
                }
                .decodeList<Usuario>()
                .run {
                    println("Usuario actualizado: $this")
                }
        } catch (e: Exception) {
            throw Exception("Error actualizando usuario: ${e.message}")
        }
    }

    override suspend fun deleteUsuario(usuario: Usuario) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("idUnico", usuario.getIdUnico())
                    }
                    select()
                }
                .decodeList<Usuario>()
                .run {
                    println("Usuario eliminado: $this")
                }
        } catch (e: Exception) {
            throw Exception("Error eliminando usuario: ${e.message}")
        }
    }
}