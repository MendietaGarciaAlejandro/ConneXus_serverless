package org.connexuss.project.firebase

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.ConversacionesUsuario

interface ConversacionesUsuariosRepositorio {
    fun getConversacionesUsuarios(): Flow<List<ConversacionesUsuario>>
    fun getConversacionUsuarioPorId(id: String): Flow<ConversacionesUsuario?>
    suspend fun addConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
    suspend fun updateConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
    suspend fun deleteConversacionUsuario(conversacionUsuario: ConversacionesUsuario)
}