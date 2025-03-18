package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow

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
            .set(usuarioPrueba.copy(id = userId))
    }

    override suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba) {
        firestore.collection(nombreColeccion).document(usuarioPrueba.id).set(usuarioPrueba)
    }

    override suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba) {
        firestore.collection(nombreColeccion).document(usuarioPrueba.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { valores.random() }
            .joinToString("")
    }
}