package org.connexuss.project.supabase

import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.toJsonObject
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.put
import org.connexuss.project.encriptacion.EncriptacionCondensada
import org.connexuss.project.encriptacion.SecretoInsertado
import org.connexuss.project.encriptacion.SecretoRPC
import org.connexuss.project.misc.SupabaseAdmin

interface ISecretosRepositorio {
    suspend fun insertarSecretoConRpc(temaId: String, claveHex: String, nonceHex: String): SecretoInsertado?
    suspend fun insertarSecretoSimpleConRpc(temaId: String, claveHex: String)
    suspend fun recuperarSecretoRpc(name: String): SecretoRPC?
    suspend fun recuperarSecretoSimpleRpc(name: String): EncriptacionCondensada.VaultSecretSelect?
}

class SupabaseSecretosRepo : ISecretosRepositorio {

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

    @OptIn(SupabaseInternal::class)
    override suspend fun insertarSecretoSimpleConRpc(temaId: String, claveHex: String) {
        // Asegúrate de usar el cliente admin con service_role
        val supabaseAdmin = SupabaseAdmin.client

        // Construimos los parámetros que espera tu función insert_secret
        val params = buildJsonObject {
            put("p_name", temaId)
            put("p_secret", claveHex)
        }

            // Invo­camos la RPC tipada a SecretoInsertado
            val response = supabaseAdmin.postgrest.rpc(
                function   = "insert_secret_simple",
                parameters = params
            )
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

    override suspend fun recuperarSecretoSimpleRpc(name: String): EncriptacionCondensada.VaultSecretSelect? {
        val supabaseAdmin = SupabaseAdmin.client

        // Make the raw HTTP call
        val response = supabaseAdmin.functions.invoke(
            function = "getSimpleSecret"
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

        // Parseamos UN objeto, no una lista
        return response.body<EncriptacionCondensada.VaultSecretSelect?>()
    }
}
