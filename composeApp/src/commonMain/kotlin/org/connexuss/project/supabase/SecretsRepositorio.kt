package org.connexuss.project.supabase

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.internal.NopCollector.emit
import org.connexuss.project.encriptacion.SecretRecord
import org.connexuss.project.misc.Supabase

interface SecretsRepositorio {
    suspend fun upsertSecret(secret: SecretRecord)
    fun getSecretByName(name: String): Flow<SecretRecord?>
}

class SupabaseSecretsRepo : SecretsRepositorio {
    private val tabla = "vault.secrets"     // <— aquí indicamos el esquema

    override suspend fun upsertSecret(secret: SecretRecord) {
        Supabase.client
            .from(tabla)
            .upsert(secret) {
                onConflict = "id"
                returning = ReturningOption.REPRESENTATION
            }
            .decodeList<SecretRecord>()
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

