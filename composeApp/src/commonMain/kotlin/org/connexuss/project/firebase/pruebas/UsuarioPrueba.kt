package org.connexuss.project.firebase.pruebas

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPrueba(
    val idUsuarioPrueba: String,
    val nombre: String,
    val titulo: String,
    val compannia: String
)