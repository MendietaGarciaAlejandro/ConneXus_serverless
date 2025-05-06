package org.connexuss.project.supabase

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.encriptacion.SecretRecord
import org.connexuss.project.misc.Supabase

interface SecretsRepositorio {
    suspend fun upsertSecret(secret: SecretRecord)
    fun getSecretByName(name: String): Flow<SecretRecord?>
}

class SupabaseSecretsRepo : SecretsRepositorio {
    private val tabla = "vault.secrets"     // <— aquí indicamos el esquema

    override suspend fun upsertSecret(secret: SecretRecord) {
        // Performs an UPSERT and returns the upserted row
        Supabase.client
            .from(tabla)
            .upsert(secret) {
                // 1) Indica la columna de conflicto (primary key = "id")
                onConflict = "id"
                // 2) Solicita devolver la fila resultante
                select()
            }
            .decodeSingleOrNull<SecretRecord>()  // ahora recibes el SecretRecord o null :contentReference[oaicite:0]{index=0}
    }

    override fun getSecretByName(name: String): Flow<SecretRecord?> = flow {
        val found = Supabase.client
            .from(tabla)
            .select { filter { eq("name", name) } }
            .decodeList<SecretRecord>()
            .firstOrNull()
        emit(found)
    }
}
