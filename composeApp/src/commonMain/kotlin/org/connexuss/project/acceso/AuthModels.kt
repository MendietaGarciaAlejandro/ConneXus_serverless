package org.connexuss.project.acceso

import org.connexuss.project.usuario.Usuario

data class RegistroRequest(
    val nombre: String,
    val edad: Int,
    val correo: String,
    val aliasPublico: String,
    val activo: Boolean = true
)

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class RecuperacionRequest(
    val correo: String
)

data class AuthResult(
    val exito: Boolean,
    val mensaje: String,
    val usuario: Usuario? = null
)
