package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Tema

interface TemasRepositorio {
    fun getTemas(): Flow<List<Tema>>
    fun getTemaPorId(id: String): Flow<Tema?>
    suspend fun addTema(tema: Tema)
    suspend fun updateTema(tema: Tema)
    suspend fun deleteTema(tema: Tema)
}