package org.connexuss.project.usuario

import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import kotlin.random.Random

class UtilidadesUsuario {

    fun generarIdUnico(): String {
        val idUnico = Random.nextInt(0, Int.MAX_VALUE)
        return idUnico.toString()
    }

    private fun validarCorreo(correo: String): Boolean {
        return correo.contains("@")
    }

    private fun validarEdad(edad: Int): Boolean {
        return edad >= 18
    }

    fun validarNombre(nombre: String): Boolean {
        return nombre.isNotEmpty()
    }

    fun generarAlias(nombre: String): String {
        val alias = nombre.map { it.code }.joinToString("")
        return alias
    }

    fun instanciaUsuario(nombre: String, edad: Int, correo: String, aliasPublico: String, activo: Boolean): Usuario {
        val correoValido = validarCorreo(correo)
        val edadValida = validarEdad(edad)
        val nombreValido = validarNombre(nombre)
        val aliasPublicoValido = validarNombre(aliasPublico)
        if (!correoValido || !edadValida || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return Usuario(nombre, edad, correo, aliasPublico, activo, emptyList(), ConversacionesUsuario(generarIdUnico(), generarIdUnico(), listOf( Conversacion( generarIdUnico(), emptyList() ) ) ) )
    }

    //Debug: Contructor con idUnico
    fun instanciaUsuario(idUnico: String, nombre: String, edad: Int, correo: String, aliasPublico: String, activo: Boolean): Usuario {
        val idUnico = idUnico
        val correoValido = validarCorreo(correo)
        val edadValida = validarEdad(edad)
        val nombreValido = validarNombre(nombre)
        val aliasPublicoValido = validarNombre(aliasPublico)
        if (!correoValido || !edadValida || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return Usuario(idUnico, nombre, edad, correo, aliasPublico, activo, emptyList(), ConversacionesUsuario(generarIdUnico(), generarIdUnico(), listOf( Conversacion( generarIdUnico(), emptyList() ) ) ) )
    }
}