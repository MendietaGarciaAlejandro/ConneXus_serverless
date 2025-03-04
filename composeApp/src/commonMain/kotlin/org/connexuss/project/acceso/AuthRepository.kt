package org.connexuss.project.acceso

import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario

/**
 * Repositorio que encapsula la lógica de acceso a los datos de usuarios.
 * Por ahora utiliza una lista en memoria para simular la persistencia.
 */
class AuthRepository {
    // Simulación de base de datos en memoria.
    private val usuarios = mutableListOf<Usuario>()

    /**
     * Crea y almacena un nuevo usuario a partir de un RegistroRequest.
     * Se asume que UtilidadesUsuario.instanciaUsuario realiza las validaciones necesarias.
     */
    fun crearUsuario(registro: RegistroRequest): Usuario {
        val util = UtilidadesUsuario()
        val usuario = util.instanciaUsuario(
            nombre = registro.nombre,
            edad = registro.edad,
            correo = registro.correo,
            aliasPublico = registro.aliasPublico,
            activo = registro.activo
        )
        usuarios.add(usuario)
        return usuario
    }

    /**
     * Busca un usuario por su correo.
     * Devuelve el usuario encontrado o null si no existe.
     */
    fun obtenerUsuarioPorEmail(email: String): Usuario? {
        return usuarios.find { it.getCorreo() == email }
    }

    /**
     * Actualiza la "contraseña" del usuario identificándolo por su ID.
     * En este ejemplo, se simula la actualización de la contraseña mediante la modificación del alias privado.
     *
     * @param userId: String que representa el ID único del usuario.
     * @param nuevaContrasena: La nueva contraseña (o valor a encriptar) que se almacenará.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    fun actualizarContrasena(userId: String, nuevaContrasena: String): Boolean {
        val usuario = usuarios.find { it.getIdUnico().toString() == userId }
        return if (usuario != null) {
            // En un escenario real, aplicarías un hash a la contraseña. Aquí se simula asignándola directamente.
            usuario.setAliasPrivado(nuevaContrasena)  // o: usuario.setAliasPrivado(hash(nuevaContrasena))
            true
        } else {
            false
        }
    }
}
