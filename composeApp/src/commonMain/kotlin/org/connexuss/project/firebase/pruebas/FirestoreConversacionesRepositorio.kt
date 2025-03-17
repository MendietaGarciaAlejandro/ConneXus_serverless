package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Conversacion

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