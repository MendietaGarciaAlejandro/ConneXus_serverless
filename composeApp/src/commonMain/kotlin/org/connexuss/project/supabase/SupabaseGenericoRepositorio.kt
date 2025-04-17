package org.connexuss.project.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val SUPABASE_URL = "https://yrpvwyewzsvxqwkacbao.supabase.co"
private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlycHZ3eWV3enN2eHF3a2FjYmFvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzOTIxMDEsImV4cCI6MjA1OTk2ODEwMX0.llu6uixyc3-VyOziE2GwdjoWcW16Jnez65GYWzX8esI"

// Interfaz sin el modificador inline en deleteItemGeneric:
//interface ISupabaseRepository<T : Any> {
//    fun getAll(tableName: String): Flow<List<T>>
//    fun getItem(tableName: String, queryBlock: PostgrestQueryBuilder.() -> Unit): Flow<T?>
//    suspend fun addItem(tableName: String, item: T)
//    suspend fun updateItem(tableName: String, updateData: Map<String, Any>, idField: String, idValue: Any)
//    suspend fun deleteItemGeneric(tableName: String, idField: String, idValue: Any)
//}

// Funcion que servirá para instanciar el supabaseClient con los parámetros necesarios
fun instanciaSupabaseClient(
    tieneStorage: Boolean = true,
    tieneAuth: Boolean = true,
    tieneRealtime: Boolean = true,
    tienePostgrest: Boolean = true
): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        if (tieneStorage) install(Storage)
        if (tieneAuth) install(Auth)
        if (tieneRealtime) install(Realtime)
        if (tienePostgrest) install(Postgrest)
    }
}

class SupabaseRepositorioGenerico {

    val supabaseClient = instanciaSupabaseClient(
        tieneStorage = true,
        tieneAuth = false,
        tieneRealtime = true,
        tienePostgrest = true
    )

    // Obtener todos los registros de una tabla
    inline fun <reified T : Any> getAll(tableName: String): Flow<List<T>> = flow {
        val result = supabaseClient
            .from(tableName)
            .select()
            .decodeList<T>()  // Se infiere T gracias a reified
        emit(result)
    }

    // Obtener un solo objeto aplicando un bloque de consulta (por ejemplo, filtros)
    inline fun <reified T : Any> getItem(
        tableName: String,
        crossinline queryBlock: PostgrestQueryBuilder.() -> Unit
    ): Flow<T?> = flow {
        val result = supabaseClient
            .from(tableName)
            .apply(queryBlock)
            .select(Columns.ALL)  // Usar Columns.ALL en vez de "*"
            .decodeSingleOrNull<T>()
        emit(result)
    }

    // Agregar un nuevo registro a la tabla
    suspend inline fun <reified T : Any> addItem(tableName: String, item: T) {
        // 1) Insertamos y pedimos que nos devuelvan el array de filas
        val response: List<T> = supabaseClient
            .from(tableName)
            .insert(item) {          // <— pasar un lambda aquí
                select()             // <— dentro del lambda invocas select()
            }
            .decodeList<T>()         // ahora llega un array JSON válido

        if (response.isEmpty()) {
            throw Exception("Error al insertar item en la tabla $tableName (no se devolvió nada)")
        } else {
            println("Item insertado: $response")
        }
    }

    // Actualizar registros filtrando por un campo (por ejemplo, "idUnico")
    suspend inline fun <reified T : Any> updateItem(
        tableName: String,
        updateData: T, // Cambiado de Map<String, Any> a T
        idField: String,
        idValue: Any
    ) {
        try {
            val response = supabaseClient
                .from(tableName)
                .update(updateData) {
                    filter {
                        eq(idField, idValue)
                    }
                    select()  // Se solicita devolver las filas actualizadas
                }
                .decodeList<T>()
            println("Item actualizado: $response")
        } catch (e: Exception) {
            throw Exception("Error actualizando item en $tableName: ${e.message}")
        }
    }

    // Eliminar registros filtrando por un campo (por ejemplo, "idUnico")
    suspend inline fun <reified T : Any> deleteItem(
        tableName: String,
        idField: String,
        idValue: Any
    ) {
        try {
            val response = supabaseClient
                .from(tableName)
                .delete {
                    filter {
                        eq(idField, idValue)
                    }
                    select()  // Se solicita devolver las filas eliminadas
                }
                .decodeList<T>()
            println("Item eliminado: $response")
        } catch (e: Exception) {
            throw Exception("Error eliminando item en $tableName: ${e.message}")
        }
    }
}