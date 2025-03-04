package org.connexuss.project.acceso

import org.connexuss.project.usuario.AlmacenamientoUsuario

class AuthService(
    private val almacenamientoUsuario: AlmacenamientoUsuario = AlmacenamientoUsuario(),
) {

    fun registrarUsuario(request: RegistroRequest): AuthResult {
        val errores = AuthValidator.validarRegistro(request)
        if (errores.isNotEmpty()) return AuthResult(false, errores.joinToString("; "))
        val usuario = AuthRepository().crearUsuario(request)
        return AuthResult(true, "Registro exitoso", usuario)
    }

    fun iniciarSesion(request: LoginRequest): AuthResult {
        val usuario = try {
            almacenamientoUsuario.obtenerUsuarioPorCorreo(request.correo)
        } catch (e: Exception) {
            return AuthResult(false, "Usuario no encontrado")
        }
        // Aquí se asume que el campo aliasPrivado contiene el valor "cifrado" (por ejemplo, mediante hash)
        // y se usa para validar la contraseña. En un escenario real, tendrías un campo de contraseña propiamente.
        if (usuario.getAliasPrivado() == request.contrasena) {
            return AuthResult(true, "Inicio de sesión exitoso", usuario)
        }
        return AuthResult(false, "Credenciales incorrectas")
    }

    fun recuperarContrasena(request: RecuperacionRequest): AuthResult {
        val usuario = try {
            almacenamientoUsuario.obtenerUsuarioPorCorreo(request.correo)
        } catch (e: Exception) {
            return AuthResult(false, "Usuario no encontrado")
        }
        // Aquí podrías invocar un proceso de envío de email de recuperación.
        // Por ahora, simulamos el proceso.
        return AuthResult(true, "Se ha enviado un correo de recuperación a ${request.correo}", usuario)
    }
}
