package org.connexuss.project.firebase

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.usuario.Usuario
/*
interface ConversacionesRepositorio {
    fun getConversaciones(): Flow<List<Conversacion>>
    fun getConversacionPorId(id: String): Flow<Conversacion?>
    suspend fun addConversacion(conversacion: Conversacion)
    suspend fun updateConversacion(conversacion: Conversacion)
    suspend fun deleteConversacion(conversacion: Conversacion)
}

interface ConversacionesUsuariosRepositorio {
    fun getConversacionesUsuarios(): Flow<List<ConversacionesUsuario>>
    fun getConversacionUsuarioPorId(id: String): Flow<ConversacionesUsuario?>
    suspend fun addConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
    suspend fun updateConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
    suspend fun deleteConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
}

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

interface HilosRepositorio {
    fun getHilos(): Flow<List<Hilo>>
    fun getHiloPorId(id: String): Flow<Hilo?>
    suspend fun addHilo(hilo: Hilo)
    suspend fun updateHilo(hilo: Hilo)
    suspend fun deleteHilo(hilo: Hilo)
}

interface MensajesRepositorio {
    fun getMensaje(): Flow<List<Mensaje>>
    fun getMensaje(id: String): Flow<Mensaje?>
    suspend fun addMensaje(chat: Mensaje)
    suspend fun updateMensaje(chat: Mensaje)
    suspend fun deleteMensaje(chat: Mensaje)
}

interface PostsRepositorio {
    fun getPosts(): Flow<List<Post>>
    fun getPostPorId(id: String): Flow<Post?>
    suspend fun addPost(post: Post)
    suspend fun updatePost(post: Post)
    suspend fun deletePost(post: Post)
}

interface TemasRepositorio {
    fun getTemas(): Flow<List<Tema>>
    fun getTemaPorId(id: String): Flow<Tema?>
    suspend fun addTema(tema: Tema)
    suspend fun updateTema(tema: Tema)
    suspend fun deleteTema(tema: Tema)
}

interface UsuariosRepositorio {
    fun getUsuario(): Flow<List<UsuarioPrueba>>
    fun getUsuarioPorId(id: String): Flow<UsuarioPrueba?>
    suspend fun addUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba)
}

interface UsuariosNuestrosRepositorio {
    fun getUsuario(): Flow<List<Usuario>>
    fun getUsuarioPorId(id: String): Flow<Usuario?>
    fun getUsuarioPorCorreo(correo: String): Flow<Usuario?>
    suspend fun addUsuario(usuario: Usuario)
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(usuario: Usuario)
}
 */