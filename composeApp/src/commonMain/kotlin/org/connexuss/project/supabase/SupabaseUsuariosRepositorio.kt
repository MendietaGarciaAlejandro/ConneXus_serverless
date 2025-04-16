package org.connexuss.project.supabase

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.usuario.Usuario

// Interfaz para simular una aplicacion CRUD que comunica con Supabase
interface ISupabaseUsuariosRepositorio {
    fun getUsuarios(): Flow<List<Usuario>>
    fun getUsuarioPorId(id: String): Flow<Usuario?>
    fun getUsuarioPorIdBis(nombre: String): Flow<Usuario?>
    suspend fun getUsuarioPorEmail(email: String): Flow<Usuario?>
    suspend fun getUsuarioPorEmailBis(email: String): Flow<Usuario?>
    suspend fun addUsuario(usuario: Usuario)
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(usuario: Usuario)
}

class SupabaseUsuariosRepositorio : ISupabaseUsuariosRepositorio {

    private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "usuario"

    override fun getUsuarios() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        emit(response)
    }

    override fun getUsuarioPorId(id: String) = flow {
        // Construye la URL con el parámetro de consulta para filtrar por id
        val tableWithFilter = "$nombreTabla?idUnico=eq.$id"
        val response = supabaseClient
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Usuario>()
        emit(response)
    }

    override fun getUsuarioPorIdBis(nombre: String) = flow {
        // Recojo todos los usuarios
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        // FIltro por id de entre todos los usuarios
        val usuario = response.find { it.getIdUnicoMio() == nombre }
        emit(usuario)
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

    override suspend fun getUsuarioPorEmailBis(email: String) = flow {
        // Recojo todos los usuarios
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        // FIltro por id de entre todos los usuarios
        val usuario = response.find { it.getCorreoMio() == email }
        emit(usuario)
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
            "nombre" to usuario.getNombreCompletoMio(),
            "correo" to usuario.getCorreoMio(),
            "contrasennia" to usuario.getContrasenniaMio(),
            "idUnico" to usuario.getIdUnicoMio()
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("idUnico", usuario.getIdUnicoMio())
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
                        eq("idUnico", usuario.getIdUnicoMio())
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