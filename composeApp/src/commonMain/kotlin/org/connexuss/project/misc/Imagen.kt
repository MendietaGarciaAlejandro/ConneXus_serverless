@file:OptIn(ExperimentalUuidApi::class)
package org.connexuss.project.misc

import org.connexuss.project.comunicacion.generateId
import org.jetbrains.compose.resources.DrawableResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Representa una imagen asociado a un identificador único.
 *
 * @property id Identificador único generado automáticamente.
 * @property imagen Recurso gráfico asociado a la imagen.
 */
data class Imagen(
    val id: Uuid = generateId(),
    val imagen: DrawableResource
) {
    // Alias para el recurso gráfico asociado a la imagen.
    val resource: DrawableResource = imagen
}