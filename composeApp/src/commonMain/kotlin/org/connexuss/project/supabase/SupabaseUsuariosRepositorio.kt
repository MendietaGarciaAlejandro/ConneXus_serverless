package org.connexuss.project.supabase

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.encriptacion.CredencialesUsuario
import org.connexuss.project.misc.Supabase
import org.connexuss.project.usuario.Usuario

// Interfaz para simular una aplicacion CRUD que comunica con Supabase
interface ISupabaseUsuariosRepositorio {
    fun getUsuarios(): Flow<List<Usuario>>
    fun getCredenciales(): Flow<List<CredencialesUsuario>>
    fun getUsuarioPorId(id: String): Flow<Usuario?>
    fun getUsuarioPorIdBis(nombre: String): Flow<Usuario?>
    fun getCredencialesByUserId(userId: String): Flow<CredencialesUsuario?>
    suspend fun addCredenciales(credenciales: CredencialesUsuario)
    suspend fun getUsuarioPorEmail(email: String): Flow<Usuario?>
    suspend fun getUsuarioPorEmailBis(email: String): Flow<Usuario?>
    suspend fun addUsuario(usuario: Usuario)
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(usuario: Usuario)
}

class SupabaseUsuariosRepositorio : ISupabaseUsuariosRepositorio {

    //private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = true, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "usuario"

    override fun getUsuarios() = flow {
        val response = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        emit(response)
    }

    override fun getCredenciales() = flow {
        val response = Supabase.client
            .from("credenciales_usuario")
            .select()
            .decodeList<CredencialesUsuario>()
        emit(response)
    }

    override fun getUsuarioPorId(id: String) = flow {
        // Construye la URL con el parámetro de consulta para filtrar por id
        val tableWithFilter = "$nombreTabla?idUnico=eq.$id"
        val response = Supabase.client
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Usuario>()
        emit(response)
    }

    override fun getUsuarioPorIdBis(nombre: String) = flow {
        // Recojo todos los usuarios
        val response = Supabase.client
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
        val response = Supabase.client
            .from(tableWithFilter)
            .select(Columns.ALL)
            .decodeSingleOrNull<Usuario>()
        emit(response)
    }

    override suspend fun getUsuarioPorEmailBis(email: String) = flow {
        // Recojo todos los usuarios
        val response = Supabase.client
            .from(nombreTabla)
            .select()
            .decodeList<Usuario>()
        // FIltro por id de entre todos los usuarios
        val usuario = response.find { it.getCorreoMio() == email }
        emit(usuario)
    }

    override fun getCredencialesByUserId(userId: String) = flow {
        // Recojo todasl las credenciales
        val response = Supabase.client
            .from("credenciales_usuario")
            .select()
            .decodeList<CredencialesUsuario>()
        // FFiltro por id de entre todas las credenciales
        val credenciales = response.find { it.idUsuario == userId }
        emit(credenciales)
    }

    override suspend fun addUsuario(usuario: Usuario) {
        try {
            val response = Supabase.client
                .from(nombreTabla)
                .insert(listOf(usuario))
                .decodeSingleOrNull<Usuario>()

            println("Usuario insertado: $response")

            if (response == null) {
                throw Exception("Error al agregar el usuario: la respuesta fue nula")
            }

        } catch (e: Exception) {
            println("Error insertando usuario: ${e.message}")
            throw e
        }
    }

    override suspend fun addCredenciales(credenciales: CredencialesUsuario) {
        try {
            val response = Supabase.client
                .from("credenciales_usuario")
                .insert(listOf(credenciales))
                .decodeSingleOrNull<CredencialesUsuario>()

            println("Credenciales insertadas: $response")

            if (response == null) {
                throw Exception("Error al agregar las credenciales: la respuesta fue nula")
            }
        } catch (e: Exception) {
            println("Error insertando credenciales: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUsuario(usuario: Usuario) {
        val updateData = mapOf(
            "nombre" to usuario.getNombreCompletoMio(),
            "correo" to usuario.getCorreoMio(),
            "contrasennia" to usuario.getContrasenniaMio(),
            "aliasprivado" to usuario.getAliasPrivadoMio(),
            "aliaspublico" to usuario.getAliasMio(),
            "descripcion" to usuario.getDescripcionMio(),
            "activo" to usuario.getActivoMio()
        )

        try {
            println("updateData: $updateData")
            println(" idunico: ${usuario.getIdUnicoMio()}")

            val updated = Supabase.client
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("idunico", usuario.getIdUnicoMio()) // ¡asegúrate que es minúscula!
                    }
                    select(Columns.ALL)
                }
                .decodeSingleOrNull<Usuario>()

            if (updated != null) {
                println("Usuario actualizado correctamente: $updated")
            } else {
                println(" No se actualizó ningún usuario")
            }

        } catch (e: Exception) {
            println(" Error al actualizar usuario: ${e.message}")
            throw e
        }
    }




    override suspend fun deleteUsuario(usuario: Usuario) {
        try {
            Supabase.client
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


    suspend fun getUsuarioAutenticado(): Usuario {
        val uid = Supabase.client.auth.currentUserOrNull()?.id
            ?: throw Exception("No hay sesión activa")

        return Supabase.client
            .from("usuario")
            .select {
                filter {
                    eq("idunico", uid)
                }
            }
            .decodeSingle<Usuario>()
    }
}