package org.connexuss.project.comunicacion

import org.connexuss.project.comunicacion.ChatMessage
import org.connexuss.project.comunicacion.ChatRoom

interface ChatRepository {
    suspend fun sendMessage(roomId: String, message: ChatMessage): Boolean
    suspend fun getChatRoom(roomId: String): ChatRoom?
    // Podrías agregar funciones para crear salas, listar salas, etc.
}
