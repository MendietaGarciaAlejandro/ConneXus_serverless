package org.connexuss.project.supabase

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.connexuss.project.encriptacion.Secreto
import org.connexuss.project.encriptacion.SecretoRPC
import org.connexuss.project.encriptacion.SecretoInsertado
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.SupabaseAdmin

interface ISecretosRepositorio {
    suspend fun upsertSecret(secret: Secreto)
    suspend fun upsertSecretAdmin(secret: Secreto)
    fun getSecretByName(name: String): Flow<Secreto?>
    fun getSecretByNameAdmin(name: String): Flow<Secreto?>
    suspend fun insertarSecretoConRpc(temaId: String, claveHex: String, nonceHex: String): SecretoInsertado?
    suspend fun recuperarSecretoRpc(temaId: String): SecretoRPC?
}

class SupabaseSecretosRepo : ISecretosRepositorio {
    private val tablaSecretos = "vault.secrets"     // <— aquí indicamos el esquema
    private val vistaSecretosDescifrados = "vault.decrypted_secrets"     // <— aquí indicamos el esquema

    override suspend fun upsertSecret(secret: Secreto) {
        // Performs an UPSERT and returns the upserted row
        Supabase.client
            .from(tablaSecretos)
            .upsert(secret) {
                // 1) Indica la columna de conflicto (primary key = "id")
                onConflict = "id"
                // 2) Solicita devolver la fila resultante
                select()
            }
            .decodeSingleOrNull<Secreto>()  // ahora recibes el SecretRecord o null :contentReference[oaicite:0]{index=0}
    }

    override suspend fun upsertSecretAdmin(secret: Secreto) {
        // Performs an UPSERT and returns the upserted row
        SupabaseAdmin.client
            .from(tablaSecretos)
            .upsert(secret) {
                // 1) Indica la columna de conflicto (primary key = "id")
                onConflict = "id"
                // 2) Solicita devolver la fila resultante
                select()
            }
            .decodeSingleOrNull<Secreto>()  // ahora recibes el SecretRecord o null :contentReference[oaicite:0]{index=0}
    }

    override fun getSecretByName(name: String): Flow<Secreto?> = flow {
        val found = Supabase.client
            .from(vistaSecretosDescifrados)
            .select { filter { eq("name", name) } }
            .decodeList<Secreto>()
            .firstOrNull()
        emit(found)
    }

    override fun getSecretByNameAdmin(name: String): Flow<Secreto?> = flow {
        val found = SupabaseAdmin.client
            .from(vistaSecretosDescifrados)
            .select { filter { eq("name", name) } }
            .decodeList<Secreto>()
            .firstOrNull()
        emit(found)
    }

    override suspend fun insertarSecretoConRpc(temaId: String, claveHex: String, nonceHex: String): SecretoInsertado? {
        // Asegúrate de usar el cliente admin con service_role
        val supabaseAdmin = SupabaseAdmin.client

        // Construimos los parámetros que espera tu función insert_secret
        val params = buildJsonObject {
            put("name", temaId)
            put("secret", claveHex)
            put("nonce", nonceHex)
        }

        // Invo­camos la RPC tipada a SecretoInsertado
        return supabaseAdmin.postgrest.rpc(
                function   = "insert_secret",
                parameters = params
            )
            .decodeSingleOrNull<SecretoInsertado>()  // ahora recibes el SecretRecord o null
            .also { response ->
                if (response == null) {
                    println("Error al insertar el secreto")
                } else {
                    println("Secreto insertado: $response")
                }
            }
    }

    override suspend fun recuperarSecretoRpc(name: String): SecretoRPC? {
        val params = buildJsonObject { put("p_name", name) }

        return SupabaseAdmin.client.postgrest
            .rpc("get_decrypted_secret", params)
            .decodeSingleOrNull<SecretoRPC>()
    }
}
