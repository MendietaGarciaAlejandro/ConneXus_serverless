package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Conversacion

interface ConversacionesRepositorio {
    fun getConversaciones(): Flow<List<Conversacion>>
    fun getConversacionPorId(id: String): Flow<Conversacion?>
    suspend fun addConversacion(conversacion: Conversacion)
    suspend fun updateConversacion(conversacion: Conversacion)
    suspend fun deleteConversacion(conversacion: Conversacion)
}