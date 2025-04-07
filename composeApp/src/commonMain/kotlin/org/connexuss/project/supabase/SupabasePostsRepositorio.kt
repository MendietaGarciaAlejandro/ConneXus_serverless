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

    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://riydmqawtpwmulqlbbjq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJpeWRtcWF3dHB3bXVscWxiYmpxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM2MjIyNTksImV4cCI6MjA1OTE5ODI1OX0.ShUPbRe_6yvIT27o5S7JE8h3ErIJJo-icrdQD1ugl8o",
    ) {
        install(Storage)
        //install(Auth)
        install(Realtime)
        install(Postgrest)
    }
    private val nombreTabla = "posts"

    override fun getPosts() = flow {
        val response = supabaseClient
            .from(nombreTabla)
            .select()
            .decodeList<Post>()
        emit(response)
    }

    override fun getPostPorId(idPost: String) = flow {
        val tableWithFilter = "$nombreTabla?id=eq.$idPost"
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
            "senderId" to post.senderId,
            "receiverId" to post.receiverId,
            "content" to post.content,
            "fechaPost" to post.fechaPost
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