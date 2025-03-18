package org.connexuss.project.comunicacion

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

/*
interface ChatRepository {
    suspend fun sendMessage(roomId: String, message: Mensaje): Boolean
    suspend fun getChatRoom(roomId: String): Conversacion?
    // Podrías agregar funciones para crear salas, listar salas, etc.
}

class ChatService(private val chatRepository: ChatRepository) {

    // Envía un mensaje a una sala específica.
    suspend fun enviarMensaje(roomId: String, message: Mensaje): Boolean {
        // Aquí puedes aplicar lógica de cifrado al contenido antes de enviarlo
        // Ejemplo: val mensajeCifrado = cifrarMensaje(message.content)
        return chatRepository.sendMessage(roomId, message)
    }

    // Obtiene la sala de chat, idealmente con un flujo (Flow) para recibir actualizaciones en tiempo real
    suspend fun obtenerSalaChat(roomId: String): Conversacion? {
        return chatRepository.getChatRoom(roomId)
    }

    // Ejemplo de función para escuchar cambios en una sala (esto se puede implementar usando Flow o callback)
    fun escucharMensajes(roomId: String): Flow<List<Mensaje>> {
        // Podrías usar Firebase Firestore snapshot listener y convertirlo en un Flow.
        TODO("Implementar la escucha de mensajes en tiempo real")
    }
}
 */

@Serializable
data class Mensaje(
    val id: String = generateRandomId(),
    val senderId: String,
    val receiverId: String,
    val content: String, // Se puede almacenar encriptado
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
    val fechaMensaje: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)//la fecha no se si funciona...

@Serializable
data class Conversacion(
    val id: String = generateRandomId(), // Valor por defecto: aleatorio
    val participants: List<String>, // IDs de los usuarios
    val messages: List<Mensaje> = emptyList(),
    val nombre: String? = null           // Nombre del chat (solo para grupos)
) {
    val grupo: Boolean
        get() = participants.size > 2
}

@Serializable
data class ConversacionesUsuario(
    val id: String,
    val idUser: String,
    val conversaciones: List<Conversacion> = emptyList(),
)

//Debug: generar randomID para conversacion
fun generateRandomId(length: Int = 10): String {
    val charset = ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}