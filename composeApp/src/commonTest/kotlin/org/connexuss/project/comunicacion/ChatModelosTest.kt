package org.connexuss.project.comunicacion

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLength
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ChatModelosTest : FunSpec({

    test("generar ID único") {
        val id = generateId()
        id shouldHaveLength 20
    }

    test("crear mensaje con datos válidos") {
        val mensaje = Mensaje(
            content = "Hola, este es un mensaje de prueba",
            idusuario = generateId(),
            idconversacion = generateId()
        )

        mensaje.id shouldNotBe null
        mensaje.content shouldBe "Hola, este es un mensaje de prueba"
        mensaje.fechaMensaje shouldNotBe null
    }

    test("crear conversación con datos válidos") {
        val conversacion = Conversacion(
            nombre = "Grupo de prueba"
        )

        conversacion.id shouldNotBe null
        conversacion.nombre shouldBe "Grupo de prueba"
    }

    test("crear conversación sin nombre") {
        val conversacion = Conversacion()

        conversacion.id shouldNotBe null
        conversacion.nombre shouldBe null
    }

    test("crear conversación de usuario") {
        val conversacionUsuario = ConversacionesUsuario(
            idusuario = generateId(),
            idconversacion = generateId()
        )

        conversacionUsuario.idusuario shouldNotBe null
        conversacionUsuario.idconversacion shouldNotBe null
    }

    test("verificar formato de fecha en mensaje") {
        val mensaje = Mensaje(
            content = "Mensaje con fecha",
            idusuario = generateId(),
            idconversacion = generateId()
        )

        val ahora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        mensaje.fechaMensaje.year shouldBe ahora.year
        mensaje.fechaMensaje.month shouldBe ahora.month
        mensaje.fechaMensaje.dayOfMonth shouldBe ahora.dayOfMonth
    }
})

