package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.*

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

/**
 * Representa un mensaje en el chat.
 *
 * @property id Identificador único del mensaje.
 * @property senderId Identificador del remitente.
 * @property receiverId Identificador del receptor.
 * @property content Contenido del mensaje (puede estar encriptado).
 * @property fechaMensaje Fecha y hora en que se creó el mensaje.
 */
@Serializable
data class Mensaje (
    val id: String = generateId(),
    val senderId: String,
    val receiverId: String,
    val content: String,
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
    val fechaMensaje: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

/**
 * Representa una conversación en el chat.
 *
 * @property id Identificador único de la conversación.
 * @property participants Lista de identificadores de los participantes.
 * @property messages Lista de mensajes dentro de la conversación.
 * @property nombre Nombre del chat (solo para grupos).
 * @property grupo Indica si la conversación es grupal, determinada por el número de participantes.
 */
@Serializable
data class Conversacion (
    val id: String = generateId(),
    val participants: List<String>,
    val messages: List<Mensaje> = emptyList(),
    val nombre: String? = null
) {
    /**
     * Indica si la conversación es grupal.
     *
     * Retorna `true` si la lista de participantes contiene más de dos usuarios.
     */
    val grupo: Boolean
        get() = participants.size > 2
}

/**
 * Representa las conversaciones asociadas a un usuario.
 *
 * @property id Identificador de la entrada.
 * @property idUser Identificador del usuario.
 * @property conversaciones Lista de conversaciones del usuario.
 */
@Serializable
data class ConversacionesUsuario(
    val id: String,
    val idUser: String,
    val conversaciones: List<Conversacion> = emptyList(),
)

/**
 * Genera un identificador único.
 *
 * @return String generado aleatoriamente.
 */
fun generateId(): String {
    val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val longitud = 20
    return (1..longitud)
        .map { valores.random() }
        .joinToString("")
}