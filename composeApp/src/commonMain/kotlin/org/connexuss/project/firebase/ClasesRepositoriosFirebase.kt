package org.connexuss.project.firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.usuario.Usuario
import kotlin.uuid.ExperimentalUuidApi
/*
@Serializable
data class UsuarioPrueba(
    val idUsuarioPrueba: String,
    val nombre: String,
    val titulo: String,
    val compannia: String
)

class FirestoreConversacionesRepositorio : ConversacionesRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "CONVERSACIONES"

    override fun getConversaciones()= flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val conversaciones = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Conversacion>()
            }
            emit(conversaciones)
        }
    }

    override fun getConversacionPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Conversacion>())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addConversacion(conversacion: Conversacion) {
        val conversacionId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(conversacionId)
            .set(conversacion.copy(id = conversacionId))
    }

    override suspend fun updateConversacion(conversacion: Conversacion) {
        firestore.collection(nombreColeccion).document(conversacion.id).set(conversacion)
    }

    override suspend fun deleteConversacion(conversacion: Conversacion) {
        firestore.collection(nombreColeccion).document(conversacion.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}

class FirestoreConversacionesUsuariosRepositorio : ConversacionesUsuariosRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "CONVERSACIONES_USUARIOS"

    override fun getConversacionesUsuarios() = flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val conversacionesUsuarios = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<ConversacionesUsuario>()
            }
            emit(conversacionesUsuarios)
        }
    }

    override fun getConversacionUsuarioPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<ConversacionesUsuario>())
        }
    }

    override suspend fun addConversacionUsuario(conversacionUsuario: ConversacionesUsuario) {
        val conversacionUsuarioId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(conversacionUsuarioId)
            .set(conversacionUsuario.copy(id = conversacionUsuarioId))
    }

    override suspend fun updateConversacionUsuario(conversacionUsuario: ConversacionesUsuario) {
        firestore.collection(nombreColeccion).document(conversacionUsuario.id).set(conversacionUsuario)
    }

    override suspend fun deleteConversacionUsuario(conversacionUsuario: ConversacionesUsuario) {
        firestore.collection(nombreColeccion).document(conversacionUsuario.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
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

class FirestoreHilosRepositorio : HilosRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "HILOS"

    override fun getHilos()= flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val hilos = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Hilo>()
            }
            emit(hilos)
        }
    }

    override fun getHiloPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Hilo>())
        }
    }

    override suspend fun addHilo(hilo: Hilo) {
        val hiloId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(hiloId)
            .set(hilo.copy(idHilo = hiloId))
    }

    override suspend fun updateHilo(hilo: Hilo) {
        firestore.collection(nombreColeccion).document(hilo.idHilo).set(hilo)
    }

    override suspend fun deleteHilo(hilo: Hilo) {
        firestore.collection(nombreColeccion).document(hilo.idHilo).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}

class FirestoreMensajesRepositorio : MensajesRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "MENSAJES"

    override fun getMensaje() = flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val chats = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Mensaje>()
            }
            emit(chats)
        }
    }

    override fun getMensaje(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Mensaje>())
        }
    }

    override suspend fun addMensaje(chat: Mensaje) {
        val chatId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(chatId)
            .set(chat.copy(id = chatId))
    }

    override suspend fun updateMensaje(chat: Mensaje) {
        firestore.collection(nombreColeccion).document(chat.id).set(chat)
    }

    override suspend fun deleteMensaje(chat: Mensaje) {
        firestore.collection(nombreColeccion).document(chat.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}

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

class FirestoreTemasRepositorio : TemasRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "TEMAS"

    override fun getTemas()= flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val temas = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Tema>()
            }
            emit(temas)
        }
    }

    override fun getTemaPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Tema>())
        }
    }

    override suspend fun addTema(tema: Tema) {
        val temaId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(temaId)
            .set(tema.copy(idTema = temaId))
    }

    override suspend fun updateTema(tema: Tema) {
        firestore.collection(nombreColeccion).document(tema.idTema).set(tema)
    }

    override suspend fun deleteTema(tema: Tema) {
        firestore.collection(nombreColeccion).document(tema.idTema).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}

class FirestoreUsuariosRepositorio : UsuariosRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion: String = "USERS"

    override fun getUsuario() = flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            val usuarioPruebas = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<UsuarioPrueba>()
            }
            emit(usuarioPruebas)
        }
    }

    override fun getUsuarioPorId(id: String) = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<UsuarioPrueba>())
        }
    }

    override suspend fun addUsuario(usuarioPrueba: UsuarioPrueba) {
        val userId = generateRandomStringId()
        firestore.collection(nombreColeccion)
            .document(userId)
            .set(usuarioPrueba.copy(idUsuarioPrueba = userId))
    }

    override suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba) {
        firestore.collection(nombreColeccion).document(usuarioPrueba.idUsuarioPrueba).set(usuarioPrueba)
    }

    override suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba) {
        firestore.collection(nombreColeccion).document(usuarioPrueba.idUsuarioPrueba).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}

class FirestoreUsuariosNuestros : UsuariosNuestrosRepositorio {

    private val firestore = Firebase.firestore
    private val nombreColeccion = "USUARIOS"

    override fun getUsuario(): Flow<List<Usuario>> = flow {
        firestore.collection(nombreColeccion).snapshots.collect { querySnapshot ->
            // Asegúrate de contar con una función de extensión o usar métodos de conversión apropiados
            val usuarios = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.data<Usuario>()
            }
            emit(usuarios)
        }
    }

    override fun getUsuarioPorId(id: String): Flow<Usuario?> = flow {
        firestore.collection(nombreColeccion).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Usuario>())
        }
    }
    override fun getUsuarioPorCorreo(correo: String): Flow<Usuario?> = flow {
        firestore.collection(nombreColeccion)
            .where { "correo" equalTo correo }
            .snapshots.collect { querySnapshot ->
                val usuario = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.data<Usuario>()
                }.firstOrNull()
                emit(usuario)
            }
    }

    override suspend fun addUsuario(usuario: Usuario) {
        val userId = generateRandomStringId()
        usuario.setIdUnico(userId)
        firestore.collection(nombreColeccion)
            .document(userId)
            .set(usuario)
    }

    override suspend fun updateUsuario(usuario: Usuario) {
        firestore.collection(nombreColeccion)
            .document(usuario.getIdUnico())
            .set(usuario)
    }

    override suspend fun deleteUsuario(usuario: Usuario) {
        firestore.collection(nombreColeccion)
            .document(usuario.getIdUnico())
            .delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}
 */