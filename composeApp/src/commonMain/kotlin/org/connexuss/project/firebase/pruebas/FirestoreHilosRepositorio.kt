package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Hilo

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