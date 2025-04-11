package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val idPost: String = generateId(),
    val senderId: String,
    val receiverId: String,
    val content: String,
    val fechaPost: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Hilo(
    val idHilo: String = generateId(),
    val idForeros: List<String>,
    val posts: List<Post> = emptyList(),
    val nombre: String? = null
)

@Serializable
data class Tema(
    val idTema: String = generateId(),
    val idUsuario: String,
    val nombre: String,
    val hilos: List<Hilo> = emptyList()
)

// NO BORRAR!!! Son las clases desarrolladas para implementar en la base de datos
/*

@Serializable
data class Tema(
    val idTema: String = generateId(),
    val nombre: String,
    val hilos: List<Hilo> = emptyList()  // Relación 1:N (cada Hilo tiene FK a Tema)
)

@Serializable
data class Hilo(
    val idHilo: String = generateId(),
    val nombre: String? = null,
    val idTema: String,                // FK a Tema.idTema
    val posts: List<Post> = emptyList() // Relación 1:N con Post
)

@Serializable
data class Post(
    val idPost: String = generateId(),
    val content: String,
    val fechaPost: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val aliasPublico: String,          // Firma pública del autor tal como se muestra en el foro
    val idHilo: String,                // FK a Hilo.idHilo
    val idFirmante: String? = null     // Opcional: referencia al Usuario que firma el post (USUARIO.idUnico)
)

 */