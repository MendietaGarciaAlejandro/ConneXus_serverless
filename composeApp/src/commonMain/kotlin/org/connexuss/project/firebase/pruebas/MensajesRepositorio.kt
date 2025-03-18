package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Mensaje

interface MensajesRepositorio {
    fun getMensaje(): Flow<List<Mensaje>>
    fun getMensaje(id: String): Flow<Mensaje?>
    suspend fun addMensaje(chat: Mensaje)
    suspend fun updateMensaje(chat: Mensaje)
    suspend fun deleteMensaje(chat: Mensaje)
}