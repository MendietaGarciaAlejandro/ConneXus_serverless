package org.connexuss.project.supabase

import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.connexuss.project.encriptacion.SecretoInsertado
import org.connexuss.project.encriptacion.SecretoRPC
import org.connexuss.project.misc.SupabaseAdmin

interface ISecretosRepositorio {
//    suspend fun upsertSecret(secret: Secreto)
//    suspend fun upsertSecretAdmin(secret: Secreto)
//    fun getSecretByName(name: String): Flow<Secreto?>
//    fun getSecretByNameAdmin(name: String): Flow<Secreto?>
    suspend fun insertarSecretoConRpc(temaId: String, claveHex: String, nonceHex: String): SecretoInsertado?
    suspend fun recuperarSecretoRpc(name: String): SecretoRPC?
}

class SupabaseSecretosRepo : ISecretosRepositorio {
//    private val tablaSecretos = "vault.secrets"     // <— aquí indicamos el esquema
//    private val vistaSecretosDescifrados = "vault.decrypted_secrets"     // <— aquí indicamos el esquema

//    override suspend fun upsertSecret(secret: Secreto) {
//        // Performs an UPSERT and returns the upserted row
//        Supabase.client
//            .from(tablaSecretos)
//            .upsert(secret) {
//                // 1) Indica la columna de conflicto (primary key = "id")
//                onConflict = "id"
//                // 2) Solicita devolver la fila resultante
//                select()
//            }
//            .decodeSingleOrNull<Secreto>()  // ahora recibes el SecretRecord o null :contentReference[oaicite:0]{index=0}
//    }
//
//    override suspend fun upsertSecretAdmin(secret: Secreto) {
//        // Performs an UPSERT and returns the upserted row
//        SupabaseAdmin.client
//            .from(tablaSecretos)
//            .upsert(secret) {
//                // 1) Indica la columna de conflicto (primary key = "id")
//                onConflict = "id"
//                // 2) Solicita devolver la fila resultante
//                select()
//            }
//            .decodeSingleOrNull<Secreto>()  // ahora recibes el SecretRecord o null :contentReference[oaicite:0]{index=0}
//    }
//
//    override fun getSecretByName(name: String): Flow<Secreto?> = flow {
//        val found = Supabase.client
//            .from(vistaSecretosDescifrados)
//            .select { filter { eq("name", name) } }
//            .decodeList<Secreto>()
//            .firstOrNull()
//        emit(found)
//    }
//
//    override fun getSecretByNameAdmin(name: String): Flow<Secreto?> = flow {
//        val found = SupabaseAdmin.client
//            .from(vistaSecretosDescifrados)
//            .select { filter { eq("name", name) } }
//            .decodeList<Secreto>()
//            .firstOrNull()
//        emit(found)
//    }

    override suspend fun insertarSecretoConRpc(temaId: String, claveHex: String, nonceHex: String): SecretoInsertado {
        // Asegúrate de usar el cliente admin con service_role
        val supabaseAdmin = SupabaseAdmin.client

        // Construimos los parámetros que espera tu función insert_secret
        val params = buildJsonObject {
            put("p_name", temaId)
            put("p_secret", claveHex)
            put("p_nonce", nonceHex)
        }

        // Invo­camos la RPC tipada a SecretoInsertado
        return supabaseAdmin.postgrest.rpc(
                function   = "insert_secret",
                parameters = params
            )
            .decodeSingle<SecretoInsertado>()  // ahora recibes el SecretRecord o null
            .also { response ->
                println("Secreto insertado: $response")
            }
    }

    override suspend fun recuperarSecretoRpc(name: String): SecretoRPC? {
        val supabaseAdmin = SupabaseAdmin.client

        // Make the raw HTTP call
        val response = supabaseAdmin.functions.invoke(
            function = "getSecret"
        ) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name))
        }

        // Check for non-200
        if (!response.status.isSuccess()) {
            throw RestException(
                "Failed to invoke getSecret: ${response.status}",
                description = "Error invoking getSecret function",
                response = response,
            )
        }

        // Deserialize the body into List<SecretoRPC>
        // Edge functions always wrap results in an array
        val arr: List<SecretoRPC> = response.body()

        // Return first element or null
        return arr.firstOrNull()
    }
}
