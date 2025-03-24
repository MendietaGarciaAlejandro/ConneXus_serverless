package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Hilo
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
    override fun getTemas(): Flow<List<Tema>> {
        TODO("Not yet implemented")
    }

    override suspend fun addTema(tema: Tema) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTema(tema: Tema) {
        TODO("Not yet implemented")
    }

    override fun getHilos(temaId: String): Flow<List<Hilo>> {
        TODO("Not yet implemented")
    }

    override suspend fun addHilo(temaId: String, hilo: Hilo) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHilo(temaId: String, hilo: Hilo) {
        TODO("Not yet implemented")
    }

    override fun getPosts(hiloId: String): Flow<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPost(hiloId: String, post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(hiloId: String, post: Post) {
        TODO("Not yet implemented")
    }
}