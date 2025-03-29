package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connexuss.project.usuario.Usuario

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