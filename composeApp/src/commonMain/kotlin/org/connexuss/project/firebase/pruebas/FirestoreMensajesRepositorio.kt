package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Mensaje

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