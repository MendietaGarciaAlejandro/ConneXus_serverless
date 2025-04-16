package org.connexuss.project.comunicacion

import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UtilidadesUsuario

/**
 * Solicitud para registrar un usuario.
 *
 * @property nombre Nombre completo del usuario.
 * @property correo Correo electrónico del usuario.
 * @property aliasPrivado Valor privado asociado al usuario.
 * @property aliasPublico Nombre público del usuario.
 * @property activo Indica si el usuario está activo.
 */
data class PeticionRegistro(
    val nombre: String,
    val correo: String,
    val aliasPrivado: String,
    val aliasPublico: String,
    val activo: Boolean = true
)

/**
 * Solicitud para iniciar sesión.
 *
 * @property correo Correo electrónico del usuario.
 * @property contrasena Contraseña del usuario.
 */
data class PeticionLogin(
    val correo: String,
    val contrasena: String
)

/**
 * Solicitud para recuperar la contraseña.
 *
 * @property correo Correo electrónico del usuario.
 */
data class PeticionRecuperacion(
    val correo: String
)

/**
 * Resultado de una operación de autorización.
 *
 * @property exito Indica si la operación fue exitosa.
 * @property mensaje Mensaje informativo sobre la operación.
 * @property idUsuario Identificador único del usuario, opcional.
 */
data class ResultadoAutorizacion(
    val exito: Boolean,
    val mensaje: String,
    val idUsuario: String? = null
)

/**
 * Valida los datos para la autenticación de usuarios.
 */
object AuthValidator {

    /**
     * Valida la solicitud de registro.
     *
     * @param peticion Los datos del registro.
     * @return Lista de errores encontrados; vacía si no hay errores.
     */
    fun validarRegistro(peticion: PeticionRegistro): List<String> {
        val errores = mutableListOf<String>()
        if (peticion.nombre.isBlank())
            errores.add("El nombre no puede estar vacío")
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("Correo inválido")
        if (peticion.aliasPublico.isBlank())
            errores.add("Alias público no puede estar vacío")
        return errores
    }

    /**
     * Valida la solicitud de inicio de sesión.
     *
     * @param peticion Los datos de inicio de sesión.
     * @return Lista de errores encontrados; vacía si no hay errores.
     */
    fun validarLogin(peticion: PeticionLogin): List<String> {
        val errores = mutableListOf<String>()
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        if (peticion.contrasena.isBlank())
            errores.add("La contraseña no puede estar vacía")
        return errores
    }

    /**
     * Valida la solicitud de recuperación de contraseña.
     *
     * @param peticion Los datos para recuperar la contraseña.
     * @return Lista de errores encontrados; vacía si no hay errores.
     */
    fun validarRecuperacion(peticion: PeticionRecuperacion): List<String> {
        val errores = mutableListOf<String>()
        if (!peticion.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        return errores
    }
}

/**
 * Lista mutable de usuarios registrados.
 */
private val usuarios = mutableListOf<Usuario>()

/**
 * Crea un usuario a partir de una solicitud de registro y lo almacena.
 *
 * @param registro Datos de la solicitud de registro.
 * @return El usuario creado o null si ocurre un error.
 */
fun crearUsuario(registro: PeticionRegistro): Usuario? {
    val util = UtilidadesUsuario()
    val usuario = util.instanciaUsuario(
        nombre = registro.nombre,
        correo = registro.correo,
        aliasPublico = registro.aliasPublico,
        activo = registro.activo
    )
    if (usuario != null) {
        usuarios.add(usuario)
    }
    return usuario
}

/**
 * Obtiene un usuario filtrado por correo electrónico.
 *
 * @param correo Correo electrónico a buscar.
 * @return El usuario encontrado o null si no existe.
 */
fun obtenerUsuarioPorEmail(correo: String): Usuario? {
    return usuarios.find { it.getCorreoMio() == correo }
}

/**
 * Actualiza la contraseña de un usuario identificado por su ID.
 *
 * @param userId Identificador único del usuario.
 * @param nuevaContrasena Nueva contraseña a asignar.
 * @return Verdadero si la actualización fue exitosa, falso en caso contrario.
 */
fun actualizarContrasena(userId: String, nuevaContrasena: String): Boolean {
    val usuario = usuarios.find { it.getIdUnicoMio().toString() == userId }
    return if (usuario != null) {
        usuario.setAliasPrivadoMio(nuevaContrasena)
        true
    } else {
        false
    }
}

/**
 * Registra un nuevo usuario utilizando la solicitud de registro.
 *
 * @param peticion Datos para el registro.
 * @param almacenamientoUsuario Repositorio que maneja el almacenamiento de usuarios.
 * @return Resultado de la operación de registro.
 */
fun registrarUsuario(peticion: PeticionRegistro, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val errores = AuthValidator.validarRegistro(peticion)
    if (errores.isNotEmpty()) return ResultadoAutorizacion(false, errores.joinToString("; "))
    val usuario = crearUsuario(peticion)
    return if (usuario != null) {
        ResultadoAutorizacion(true, "Registro exitoso", usuario.getIdUnicoMio())
    } else {
        ResultadoAutorizacion(false, "Error al registrar usuario")
    }
}

/**
 * Inicia la sesión de un usuario utilizando la solicitud de login.
 *
 * @param peticion Datos de inicio de sesión.
 * @param almacenamientoUsuario Repositorio que maneja el almacenamiento de usuarios.
 * @return Resultado de la operación de inicio de sesión.
 */
fun iniciarSesion(peticion: PeticionLogin, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val usuario = try {
        almacenamientoUsuario.obtenerUsuarioPorCorreo(peticion.correo)
    } catch (e: Exception) {
        return ResultadoAutorizacion(false, "Usuario no encontrado")
    }
    if (usuario.getAliasPrivadoMio() == peticion.contrasena) {
        return ResultadoAutorizacion(true, "Inicio de sesión exitoso", usuario.getIdUnicoMio())
    }
    return ResultadoAutorizacion(false, "Credenciales incorrectas")
}

/**
 * Procesa la solicitud de recuperación de contraseña de un usuario.
 *
 * @param peticion Datos para la recuperación de contraseña.
 * @param almacenamientoUsuario Repositorio que maneja el almacenamiento de usuarios.
 * @return Resultado de la operación de recuperación.
 */
fun recuperarContrasena(peticion: PeticionRecuperacion, almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario()): ResultadoAutorizacion {
    val usuario = try {
        almacenamientoUsuario.obtenerUsuarioPorCorreo(peticion.correo)
    } catch (e: Exception) {
        return ResultadoAutorizacion(false, "Usuario no encontrado")
    }
    return ResultadoAutorizacion(true, "Se ha enviado un correo de recuperación a ${peticion.correo}", usuario.getIdUnicoMio())
}