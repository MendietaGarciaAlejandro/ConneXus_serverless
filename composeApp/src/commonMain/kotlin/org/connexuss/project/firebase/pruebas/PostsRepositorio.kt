package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Post

interface PostsRepositorio {
    fun getPosts(): Flow<List<Post>>
    fun getPostPorId(id: String): Flow<Post?>
    suspend fun addPost(post: Post)
    suspend fun updatePost(post: Post)
    suspend fun deletePost(post: Post)
}