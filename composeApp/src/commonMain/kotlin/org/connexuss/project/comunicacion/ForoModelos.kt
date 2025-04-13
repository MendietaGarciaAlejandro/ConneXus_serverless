package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
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
data class Post(
    @SerialName("id_post")
    val idPost: String = generateId(),

    @SerialName("sender_id")
    val senderId: String,

    @SerialName("receiver_id")
    val receiverId: String,

    @SerialName("content")
    val content: String,

    @SerialName("fecha_post")
    val fechaPost: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
)

@Serializable
data class Hilo(
    @SerialName("id_hilo")
    val idHilo: String = generateId(),

    /**
     * Se asume que 'id_foreros' es un array de texto (o JSON)
     * que almacena los IDs de los participantes del hilo.
     */
    @SerialName("id_foreros")
    val idForeros: List<String>,

    /**
     * Se asume que 'posts' es un JSON[] con la lista de `Post` en la misma tabla.
     */
    @SerialName("posts")
    val posts: List<Post> = emptyList(),

    @SerialName("nombre")
    val nombre: String? = null
)

@Serializable
data class Tema(
    @SerialName("id_tema")
    val idTema: String = generateId(),

    @SerialName("id_usuario")
    val idUsuario: String,

    @SerialName("nombre")
    val nombre: String,

    /**
     * Se asume que 'hilos' es un JSON[] en la tabla
     * que contiene la lista de `Hilo`.
     */
    @SerialName("hilos")
    val hilos: List<Hilo> = emptyList()
)
 */