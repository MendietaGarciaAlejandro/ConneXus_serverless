package org.connexuss.project.comunicacion

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Modelo actualizado para representar un post del foro.
 *
 * @property idPost Identificador único generado para el post.
 * @property idSender Identificador del usuario que envía el post.
 * @property idReceiver Identificador del usuario que recibe el post.
 * @property content Texto del mensaje del post.
 * @property fechaPost Marca temporal con la fecha y hora de creación.
 */
@Serializable
data class Post(
    val idPost: String = generateId(),
    val idSender: String,
    val idReceiver: String,
    val content: String,
    val fechaPost: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

/**
 * Modelo actualizado para representar un hilo en el foro.
 *
 * @property idHilo Identificador único generado para el hilo.
 * @property idForeros Lista de identificadores de los usuarios participantes.
 * @property idPosts Lista de identificadores de los posts asociados al hilo.
 * @property nombre Título o nombre que identifica el hilo; puede ser null.
 */
@Serializable
data class Hilo(
    val idHilo: String = generateId(),
    val idForeros: List<String>,
    val idPosts: List<String> = emptyList(),
    val nombre: String? = null
)

/**
 * Modelo actualizado para representar un tema en el foro.
 *
 * @property idTema Identificador único generado para el tema.
 * @property idUsuario Identificador del usuario que creó el tema.
 * @property nombre Nombre descriptivo del tema.
 * @property idHilos Lista de identificadores de hilos asociados al tema.
 */
@Serializable
data class Tema(
    val idTema: String = generateId(),
    val idUsuario: String,
    val nombre: String,
    val idHilos: List<String> = emptyList()
)