package org.connexuss.project.comunicacion

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.connexuss.project.usuario.AlmacenamientoUsuario

class AuthModelosTest : FunSpec({

    test("validar registro con datos válidos") {
        val peticion = PeticionRegistro(
            nombre = "Test User",
            correo = "test@example.com",
            aliasPrivado = "password123",
            aliasPublico = "testuser",
            activo = true
        )
        val errores = AuthValidator.validarRegistro(peticion)
        errores.isEmpty() shouldBe true
    }

    test("validar registro con datos inválidos") {
        val peticion = PeticionRegistro(
            nombre = "",
            correo = "invalidemail",
            aliasPrivado = "password123",
            aliasPublico = "",
            activo = true
        )
        val errores = AuthValidator.validarRegistro(peticion)
        errores.size shouldBe 3
    }

    test("validar login con credenciales válidas") {
        val peticion = PeticionLogin(
            correo = "test@example.com",
            contrasena = "password123"
        )
        val errores = AuthValidator.validarLogin(peticion)
        errores.isEmpty() shouldBe true
    }

    test("validar login con credenciales inválidas") {
        val peticion = PeticionLogin(
            correo = "invalidemail",
            contrasena = ""
        )
        val errores = AuthValidator.validarLogin(peticion)
        errores.size shouldBe 2
    }

    test("registrar usuario exitosamente") {
        val peticion = PeticionRegistro(
            nombre = "Test User",
            correo = "test@example.com",
            aliasPrivado = "password123",
            aliasPublico = "testuser",
            activo = true
        )
        val resultado = registrarUsuario(peticion)
        resultado.exito shouldBe true
        resultado.idUsuario shouldNotBe null
    }

    test("iniciar sesión exitosamente") {
        // Primero registramos un usuario
        val peticionRegistro = PeticionRegistro(
            nombre = "Test User",
            correo = "login@example.com",
            aliasPrivado = "password123",
            aliasPublico = "loginuser",
            activo = true
        )
        registrarUsuario(peticionRegistro)

        // Intentamos iniciar sesión
        val peticionLogin = PeticionLogin(
            correo = "login@example.com",
            contrasena = "password123"
        )
        val resultado = iniciarSesion(peticionLogin)
        resultado.exito shouldBe true
        resultado.idUsuario shouldNotBe null
    }

    test("recuperar contraseña con correo válido") {
        val peticion = PeticionRecuperacion(
            correo = "test@example.com"
        )
        val resultado = recuperarContrasena(peticion)
        resultado.exito shouldBe true
        resultado.mensaje.contains(peticion.correo) shouldBe true
    }
})
