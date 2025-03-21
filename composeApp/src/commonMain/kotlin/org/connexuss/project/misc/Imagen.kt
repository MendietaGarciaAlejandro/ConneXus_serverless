package org.connexuss.project.misc

import org.jetbrains.compose.resources.DrawableResource

// Clase que representa una imagen asocado a un id
data class Imagen(
    val id: String,
    val imagen: DrawableResource
)