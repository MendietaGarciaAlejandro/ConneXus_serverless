package org.connexuss.project.firebase.pruebas

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPrueba(
    val id: String,
    val name: String,
    val title: String,
    val company: String
)