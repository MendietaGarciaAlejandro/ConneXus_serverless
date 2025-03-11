package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Mensaje(
    val id: String = generateRandomId(),
    val senderId: String,
    val receiverId: String,
    val content: String, // Se puede almacenar encriptado
    //val timestamp: Long = System.currentTimeMillis() // Milisegundos desde epoch
    val fechaMensaje: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)//la fecha no se si funciona...

data class Conversacion(
    val id: String = generateRandomId(), // Valor por defecto: aleatorio
    val participants: List<String>, // IDs de los usuarios
    val messages: List<Mensaje> = emptyList()
) {
    val grupo: Boolean
        get() = participants.size > 2
}

//Debug: generar randomID para conversacion
fun generateRandomId(length: Int = 10): String {
    val charset = ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}


data class ConversacionesUsuario(
    val id: String,
    val idUser: String,
    val conversaciones: List<Conversacion> = emptyList(),
)

