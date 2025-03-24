package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema

interface FirebaseForoRepositorio {
    // Obtiene todos los temas disponibles
    fun getTemas(): Flow<List<Tema>>
    suspend fun addTema(tema: Tema)
    suspend fun updateTema(tema: Tema)

    // Obtiene todos los hilos de un tema
    fun getHilos(temaId: String): Flow<List<Hilo>>
    suspend fun addHilo(temaId: String, hilo: Hilo)
    suspend fun updateHilo(temaId: String, hilo: Hilo)

    // Obtiene los posts de un hilo
    fun getPosts(hiloId: String): Flow<List<Post>>
    suspend fun addPost(hiloId: String, post: Post)
    suspend fun updatePost(hiloId: String, post: Post)
}

class FirestoreForoRepositorio : FirebaseForoRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccionTemas: String = "TEMAS"
    private val nombreColeccionHilos: String = "HILOS"
    private val nombreColeccionPost: String = "POST"

    override fun getTemas() = flow {
        firestore.collection(nombreColeccionTemas).snapshots.collect { querySnapshot ->
            val temas = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Tema>()
            }
            emit(temas)
        }
    }

    override suspend fun addTema(tema: Tema) {
        val temaId = generateRandomStringId()
        firestore.collection(nombreColeccionTemas)
            .document(temaId)
            .set(tema.copy(idTema = temaId))
    }

    override suspend fun updateTema(tema: Tema) {
        firestore.collection(nombreColeccionTemas).document(tema.idTema).set(tema)
    }

    override fun getHilos(temaId: String) = flow {
        firestore.collection(nombreColeccionTemas).document(temaId).collection(nombreColeccionHilos).snapshots.collect { querySnapshot ->
            val hilos = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Hilo>()
            }
            emit(hilos)
        }
    }

    override suspend fun addHilo(temaId: String, hilo: Hilo) {
        val hiloId = generateRandomStringId()
        firestore.collection(nombreColeccionTemas).document(temaId).collection(nombreColeccionHilos)
            .document(hiloId)
            .set(hilo.copy(idHilo = hiloId))
    }

    override suspend fun updateHilo(temaId: String, hilo: Hilo) {
        firestore.collection(nombreColeccionTemas).document(temaId).collection(nombreColeccionHilos).document(hilo.idHilo).set(hilo)
    }

    override fun getPosts(hiloId: String) = flow {
        firestore.collection(nombreColeccionTemas).document(hiloId).collection(nombreColeccionHilos).snapshots.collect { querySnapshot ->
            val posts = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Post>()
            }
            emit(posts)
        }
    }

    override suspend fun addPost(hiloId: String, post: Post) {
        val postId = generateRandomStringId()
        firestore.collection(nombreColeccionTemas).document(hiloId).collection(nombreColeccionHilos)
            .document(postId)
            .set(post.copy(idPost = postId))
    }

    override suspend fun updatePost(hiloId: String, post: Post) {
        firestore.collection(nombreColeccionTemas).document(hiloId).collection(nombreColeccionHilos).document(post.idPost).set(post)
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}