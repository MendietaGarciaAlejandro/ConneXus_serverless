package org.connexuss.project.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Post

interface ISupabasePostsRepositorio {
    fun getPosts(): Flow<List<Post>>
    fun getPostPorId(idPost: String): Flow<Post?>
    fun getPostPorIdBis(idPost: String): Flow<Post?>
    suspend fun addPost(post: Post)
    suspend fun updatePost(post: Post)
    suspend fun deletePost(post: Post)
}

class SupabasePostsRepositorio : ISupabasePostsRepositorio {

    private val supabaseClient = instanciaSupabaseClient( tieneStorage = true, tieneAuth = false, tieneRealtime = true, tienePostgrest = true)
    private val nombreTabla = "post"

    override fun getPosts() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Post>()
        emit(response)
    }

    override fun getPostPorId(idPost: String) = flow {
        val tableWithFilter = "$nombreTabla?idpost=eq.$idPost"
        val response = supabaseClient
            .from(tableWithFilter)
            .select()
            .decodeSingleOrNull<Post>()
        emit(response)
    }

    override fun getPostPorIdBis(idPost: String) = flow {
        // Recojo todos los posts
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Post>()
        // Filtro los posts por idPost
        val filteredResponse = response.find { it.idPost == idPost }
        emit(filteredResponse)
    }

    override suspend fun addPost(post: Post) {
        val response = supabaseClient
            .from(nombreTabla)
            .insert(post)
            .decodeSingleOrNull<Post>()
        if (response == null) {
            throw Exception("Error al agregar el post")
        } else {
            println("Post agregado: $response")
        }
    }

    override suspend fun updatePost(post: Post) {
        val updateData = mapOf(
            "idPost" to post.idPost,
            "content" to post.content,
            "idHilo" to post.idHilo,
            "idFirmante" to post.idFirmante,
            "fechaPost" to post.fechaPost,
            "aliaspublico" to post.aliaspublico,
        )
        try {
            supabaseClient
                .from(nombreTabla)
                .update(updateData) {
                    filter {
                        eq("idPost", post.idPost)
                    }
                    select()
                }
                .decodeList<Post>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al actualizar el post")
                    } else {
                        println("Post actualizado: $this")
                    }
                }
        } catch (e: Exception) {
            println("Error al actualizar el post: ${e.message}")
            throw e
        }
    }

    override suspend fun deletePost(post: Post) {
        try {
            supabaseClient
                .from(nombreTabla)
                .delete {
                    filter {
                        eq("idPost", post.idPost)
                    }
                }
                .decodeList<Post>()
                .run {
                    if (isEmpty()) {
                        throw Exception("Error al eliminar el post")
                    } else {
                        println("Post eliminado: $this")
                    }
                }
        } catch (e: Exception) {
            println("Error al eliminar el post: ${e.message}")
            throw e
        }
    }
}