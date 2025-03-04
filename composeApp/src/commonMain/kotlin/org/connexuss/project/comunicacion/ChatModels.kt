package org.connexuss.project.comunicacion

data class ChatMessage(
    val id: String,
    val senderId: String,
    val content: String, // Se puede almacenar encriptado
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
)

data class ChatRoom(
    val id: String,
    val participants: List<String>, // IDs de los usuarios
    val messages: List<ChatMessage> = emptyList()
)
