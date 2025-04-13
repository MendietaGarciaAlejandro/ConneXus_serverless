package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
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

fun generateId(): String {
    val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val longitud = 20
    return (1..longitud)
        .map { valores.random() }
        .joinToString("")
}

@Serializable
data class Mensaje (
    val id: String = generateId(),
    val senderId: String,
    val receiverId: String,
    val content: String,
    val fechaMensaje: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Conversacion (
    val id: String = generateId(),
    val participants: List<String>,
    val messages: List<Mensaje> = emptyList(),
    val nombre: String? = null
) {
    /**
     * Indica si la conversación es grupal.
     * Retorna `true` si la lista de participantes contiene más de dos usuarios.
     */
    val grupo: Boolean
        get() = participants.size > 2
}

@Serializable
data class ConversacionesUsuario(
    val id: String,
    val idUser: String,
    val conversaciones: List<Conversacion> = emptyList(),
)

// NO BORRAR!!! Son las clases desarrolladas para implementar en la base de datos
/*
@Serializable
data class Mensaje(
    @SerialName("id")
    val id: String = generateId(),

    @SerialName("sender_id")
    val senderId: String,

    @SerialName("receiver_id")
    val receiverId: String,

    @SerialName("content")
    val content: String,

    @SerialName("fecha_mensaje")
    val fechaMensaje: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Conversacion(
    @SerialName("id")
    val id: String = generateId(),

    /**
     * Se asume que la columna 'participants' es un JSON[] o text[]
     * que almacena la lista de participantes.
     */
    @SerialName("participants")
    val participants: List<String>,

    /**
     * Se asume que la columna 'messages' es un JSON[] que almacena
     * la lista de mensajes en la misma tabla, lo cual es posible,
     * pero no es muy normalizado. Ajusta según tu diagrama.
     */
    @SerialName("messages")
    val messages: List<Mensaje> = emptyList(),

    @SerialName("nombre")
    val nombre: String? = null
) {
    /**
     * Indica si la conversación es grupal: más de dos participantes.
     */
    val grupo: Boolean
        get() = participants.size > 2
}

@Serializable
data class ConversacionesUsuario(
    @SerialName("id")
    val id: String,

    @SerialName("id_user")
    val idUser: String,

    /**
     * Se asume que 'conversaciones' es un JSON[] en la base de datos
     * que almacena una lista de `Conversacion`.
     */
    @SerialName("conversaciones")
    val conversaciones: List<Conversacion> = emptyList(),
)
 */