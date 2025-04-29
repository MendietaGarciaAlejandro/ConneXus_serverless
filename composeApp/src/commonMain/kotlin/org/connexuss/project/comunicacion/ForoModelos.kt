package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/*
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
 */

// NO BORRAR!!! Son las clases desarrolladas para implementar en la base de datos

@Serializable
data class Post(
    @SerialName("idpost")
    val idPost: String = generateId(),

    @SerialName("content")
    val content: String,

    @SerialName("fechapost")
    val fechaPost: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),

    @SerialName("aliaspublico")
    val aliaspublico: String,

    @SerialName("idhilo")
    val idHilo: String,

    @SerialName("idfirmante")
    val idFirmante: String
)

@Serializable
data class Hilo(
    @SerialName("idhilo")
    val idHilo: String = generateId(),

    @SerialName("nombre")
    val nombre: String? = "",

    @SerialName("idtema")
    val idTema: String? = ""
)

@Serializable
data class Tema(
    @SerialName("idtema")
    val idTema: String = generateId(),

    @SerialName("nombre")
    val nombre: String
)