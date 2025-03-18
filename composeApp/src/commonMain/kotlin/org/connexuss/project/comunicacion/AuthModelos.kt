package org.connexuss.project.comunicacion

import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario

// Modelos de datos para la autenticación de usuarios.
data class PeticionRegistro(
    val nombre: String,
    val correo: String,
    val aliasPrivado: String,
    val aliasPublico: String,
    val activo: Boolean = true
)

data class PeticionLogin(
    val correo: String,
    val contrasena: String
)

data class PeticionRecuperacion(
    val correo: String
)

data class ResultadoAutorizacion(
    val exito: Boolean,
    val mensaje: String,
    val idUsuario: String? = null
)

// Validador de datos para la autenticación de usuarios.
object AuthValidator {

    // Validación para el registro de usuario
    fun validarRegistro(peticion: PeticionRegistro): List<String> {
        val errores = mutableListOf<String>()
        if (peticion.nombre.isBlank())
            errores.add("El nombre no puede estar vacío")
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("Correo inválido")
        if (peticion.aliasPublico.isBlank())
            errores.add("Alias público no puede estar vacío")
        // Si en el futuro se agrega un campo de contraseña en el registro, se puede validar aquí:
        // if (request.contrasena.isBlank() || request.contrasena.length < 6)
        //     errores.add("La contraseña debe tener al menos 6 caracteres")
        return errores
    }

    // Validación para el inicio de sesión
    fun validarLogin(peticion: PeticionLogin): List<String> {
        val errores = mutableListOf<String>()
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        if (peticion.contrasena.isBlank())
            errores.add("La contraseña no puede estar vacía")
        // Se puede agregar más lógica, como requisitos de longitud o caracteres especiales, si es necesario.
        return errores
    }

    // Validación para la recuperación de contraseña
    fun validarRecuperacion(peticion: PeticionRecuperacion): List<String> {
        val errores = mutableListOf<String>()
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        return errores
    }
}

// Repositorio que encapsula la lógica de acceso a los datos de usuarios.
/**
 * Repositorio que encapsula la lógica de acceso a los datos de usuarios.
 * Por ahora utiliza una lista en memoria para simular la persistencia.
 */
// Simulación de base de datos en memoria.
private val usuarios = mutableListOf<Usuario>()

/**
 * Crea y almacena un nuevo usuario a partir de un RegistroRequest.
 * Se asume que UtilidadesUsuario.instanciaUsuario realiza las validaciones necesarias.
 */
fun crearUsuario(registro: PeticionRegistro): Usuario {
    val util = UtilidadesUsuario()
    val usuario = util.instanciaUsuario(
        nombre = registro.nombre,
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
fun obtenerUsuarioPorEmail(correo: String): Usuario? {
    return usuarios.find { it.getCorreo() == correo }
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

// Funciones de servicio para la autenticación de usuarios.
fun registrarUsuario(peticion: PeticionRegistro, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val errores = AuthValidator.validarRegistro(peticion)
    if (errores.isNotEmpty()) return ResultadoAutorizacion(false, errores.joinToString("; "))
    val usuario = crearUsuario(peticion)
    return ResultadoAutorizacion(true, "Registro exitoso", usuario.getIdUnico())
}

fun iniciarSesion(peticion: PeticionLogin, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val usuario = try {
        almacenamientoUsuario.obtenerUsuarioPorCorreo(peticion.correo)
    } catch (e: Exception) {
        return ResultadoAutorizacion(false, "Usuario no encontrado")
    }
    // Aquí se asume que el campo aliasPrivado contiene el valor "cifrado" (por ejemplo, mediante hash)
    // y se usa para validar la contraseña. En un escenario real, tendrías un campo de contraseña propiamente.
    if (usuario.getAliasPrivado() == peticion.contrasena) {
        return ResultadoAutorizacion(true, "Inicio de sesión exitoso", usuario.getIdUnico())
    }
    return ResultadoAutorizacion(false, "Credenciales incorrectas")
}

fun recuperarContrasena(peticion: PeticionRecuperacion, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val usuario = try {
        almacenamientoUsuario.obtenerUsuarioPorCorreo(peticion.correo)
    } catch (e: Exception) {
        return ResultadoAutorizacion(false, "Usuario no encontrado")
    }
    // Aquí podrías invocar un proceso de envío de email de recuperación.
    // Por ahora, simulamos el proceso.
    return ResultadoAutorizacion(true, "Se ha enviado un correo de recuperación a ${peticion.correo}", usuario.getIdUnico())
}