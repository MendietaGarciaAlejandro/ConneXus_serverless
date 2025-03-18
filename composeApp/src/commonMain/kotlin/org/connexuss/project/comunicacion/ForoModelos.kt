package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val idPost: String = generateRandomId(),
    val senderId: String,
    val receiverId: String,
    val content: String,
    val fechaPost: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Hilo(
    val idHilo: String = generateRandomId(),
    val idForeros: List<String>,
    val posts: List<Post> = emptyList(),
    val nombre: String? = null
)

@Serializable
data class Tema(
    val idTema: String,
    val idUsuario: String,
    val hilos: List<Hilo> = emptyList(),
)