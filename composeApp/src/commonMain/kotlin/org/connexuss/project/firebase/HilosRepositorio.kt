package org.connexuss.project.firebase

import org.connexuss.project.comunicacion.Hilo
import kotlinx.coroutines.flow.Flow

interface HilosRepositorio {
    fun getHilos(): Flow<List<Hilo>>
    fun getHiloPorId(id: String): Flow<Hilo?>
    suspend fun addHilo(hilo: Hilo)
    suspend fun updateHilo(hilo: Hilo)
    suspend fun deleteHilo(hilo: Hilo)
}