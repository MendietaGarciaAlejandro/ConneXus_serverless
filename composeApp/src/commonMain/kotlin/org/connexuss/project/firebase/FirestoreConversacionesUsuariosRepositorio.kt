package org.connexuss.project.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.ConversacionesUsuario

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