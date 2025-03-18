package org.connexuss.project.firebase.pruebas

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow
import org.connexuss.project.comunicacion.Tema

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