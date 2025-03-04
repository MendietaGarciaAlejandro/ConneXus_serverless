package org.connexuss.project.acceso

object AuthValidator {

    // Validación para el registro de usuario
    fun validarRegistro(request: RegistroRequest): List<String> {
        val errores = mutableListOf<String>()
        if (request.nombre.isBlank())
            errores.add("El nombre no puede estar vacío")
        if (request.edad < 18)
            errores.add("Debe ser mayor de edad")
        if (!request.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("Correo inválido")
        if (request.aliasPublico.isBlank())
            errores.add("Alias público no puede estar vacío")
        // Si en el futuro se agrega un campo de contraseña en el registro, se puede validar aquí:
        // if (request.contrasena.isBlank() || request.contrasena.length < 6)
        //     errores.add("La contraseña debe tener al menos 6 caracteres")
        return errores
    }

    // Validación para el inicio de sesión
    fun validarLogin(request: LoginRequest): List<String> {
        val errores = mutableListOf<String>()
        if (!request.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        if (request.contrasena.isBlank())
            errores.add("La contraseña no puede estar vacía")
        // Se puede agregar más lógica, como requisitos de longitud o caracteres especiales, si es necesario.
        return errores
    }

    // Validación para la recuperación de contraseña
    fun validarRecuperacion(request: RecuperacionRequest): List<String> {
        val errores = mutableListOf<String>()
        if (!request.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")))
            errores.add("El correo ingresado no tiene un formato válido")
        return errores
    }
}
