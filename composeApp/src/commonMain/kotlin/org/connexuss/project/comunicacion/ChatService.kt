package org.connexuss.project.comunicacion

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.ChatMessage
import org.connexuss.project.comunicacion.ChatRoom
import org.connexuss.project.comunicacion.ChatRepository

class ChatService(private val chatRepository: ChatRepository) {

    // Envía un mensaje a una sala específica.
    suspend fun enviarMensaje(roomId: String, message: ChatMessage): Boolean {
        // Aquí puedes aplicar lógica de cifrado al contenido antes de enviarlo
        // Ejemplo: val mensajeCifrado = cifrarMensaje(message.content)
        return chatRepository.sendMessage(roomId, message)
    }

    // Obtiene la sala de chat, idealmente con un flujo (Flow) para recibir actualizaciones en tiempo real
    suspend fun obtenerSalaChat(roomId: String): ChatRoom? {
        return chatRepository.getChatRoom(roomId)
    }

    // Ejemplo de función para escuchar cambios en una sala (esto se puede implementar usando Flow o callback)
    fun escucharMensajes(roomId: String): Flow<List<ChatMessage>> {
        // Podrías usar Firebase Firestore snapshot listener y convertirlo en un Flow.
        TODO("Implementar la escucha de mensajes en tiempo real")
    }
}
