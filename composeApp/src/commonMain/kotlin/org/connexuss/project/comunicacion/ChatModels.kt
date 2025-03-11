package org.connexuss.project.comunicacion

import kotlinx.datetime.LocalDateTime

data class Mensaje(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String, // Se puede almacenar encriptado
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
    val fechaMensaje: LocalDateTime
)

data class Conversacion(
    val id: String,
    val participants: List<String>, // IDs de los usuarios
    val messages: List<Mensaje> = emptyList()
) {
    val grupo: Boolean
        get() = participants.size > 2
}


data class ConversacionesUsuario(
    val id: String,
    val idUser: String,
    val conversaciones: List<Conversacion> = emptyList(),
)

