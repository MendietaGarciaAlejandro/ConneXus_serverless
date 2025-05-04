package org.connexuss.project.supabase

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.encriptacion.ClavesUsuario
import org.connexuss.project.misc.Supabase

interface ClavesUsuarioRepositorio {
    fun getClavesByUserId(idUsuario: String): Flow<ClavesUsuario?>
    suspend fun upsertClaves(claves: ClavesUsuario)
}

class SupabaseClavesRepo: ClavesUsuarioRepositorio {

    override fun getClavesByUserId(idUsuario: String): Flow<ClavesUsuario?> = flow {
        val result = Supabase.client
            .from("claves_usuario")
            .select {
                filter {
                    eq("id_usuario", idUsuario)
                }
            }
            .decodeList<ClavesUsuario>()
        emit(
            value = result.firstOrNull()
        )
    }

    override suspend fun upsertClaves(claves: ClavesUsuario) {
        // 1) Asegúrate de que tu tabla esté tipada en tu llamada .from<ClavesUsuario>(...)
        Supabase.client
            .from("claves_usuario")
            // 2) Pasa directamente tu instancia de data class
            .upsert(claves) {
                // opcional: selecciona qué columnas devolver, o onConflict:
                onConflict = "id_usuario"
            }
    }
}
