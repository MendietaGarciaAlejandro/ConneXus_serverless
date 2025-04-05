package org.connexuss.project.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Post

class FirestorePostsRepositorio : PostsRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "POSTS"

    override fun getPosts()= flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val posts = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Post>()
            }
            emit(posts)
        }
    }

    override fun getPostPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Post>())
        }
    }

    override suspend fun addPost(post: Post) {
        val postId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(postId)
            .set(post.copy(idPost = postId))
    }

    override suspend fun updatePost(post: Post) {
        firestore.collection(nombreColeccion).document(post.idPost).set(post)
    }

    override suspend fun deletePost(post: Post) {
        firestore.collection(nombreColeccion).document(post.idPost).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}