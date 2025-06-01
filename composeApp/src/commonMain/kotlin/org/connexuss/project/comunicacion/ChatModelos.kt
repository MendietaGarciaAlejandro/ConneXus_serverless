package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.*

fun generateId(): String {
    val valores = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val longitud = 20
    return (1..longitud)
        .map { valores.random() }
        .joinToString("")
}

// NO BORRAR!!! Son las clases desarrolladas para implementar en la base de datos
@Serializable
data class Mensaje(
    @SerialName("id")
    val id: String = generateId(),

    @SerialName("fechamensaje")
    val fechaMensaje: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),

    @SerialName("content")
    val content: String,

    @SerialName("idusuario")
    val idusuario: String = generateId(),

    @SerialName("idconversacion")
    val idconversacion: String,

    @SerialName("imageurl")
    val imageUrl: String? = null
)

@Serializable
data class Conversacion(
    @SerialName("id")
    val id: String = generateId(),

    @SerialName("nombre")
    val nombre: String? = null
)

@Serializable
data class ConversacionesUsuario(
    @SerialName("idusuario")
    val idusuario: String = generateId(),

    @SerialName("idconversacion")
    val idconversacion: String = generateId(),
)