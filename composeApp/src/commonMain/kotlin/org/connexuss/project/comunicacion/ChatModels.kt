package org.connexuss.project.comunicacion

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime

data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String, // Se puede almacenar encriptado
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
    val fechaMensaje: LocalDateTime
)

data class ChatRoom(
    val id: String,
    val participants: List<String>, // IDs de los usuarios
    val messages: List<ChatMessage> = emptyList()
)

data class ChatsUsers(
    val id: String,
    val idUser: String,
    val chatRoom: ChatRoom,
)
