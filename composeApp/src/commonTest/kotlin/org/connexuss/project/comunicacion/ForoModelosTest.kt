package org.connexuss.project.comunicacion

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ForoModelosTest : FunSpec({

    test("crear post con datos válidos") {
        val post = Post(
            content = "Este es un post de prueba",
            aliaspublico = "usuario_test",
            idHilo = generateId(),
            idFirmante = generateId()
        )

        post.idPost shouldNotBe null
        post.content.shouldNotBeEmpty()
        post.fechaPost shouldNotBe null
        post.aliaspublico shouldBe "usuario_test"
    }

    test("verificar fecha de creación del post") {
        val post = Post(
            content = "Post para verificar fecha",
            aliaspublico = "usuario_test",
            idHilo = generateId(),
            idFirmante = generateId()
        )

        val ahora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        post.fechaPost.year shouldBe ahora.year
        post.fechaPost.month shouldBe ahora.month
        post.fechaPost.dayOfMonth shouldBe ahora.dayOfMonth
    }

    test("crear hilo con nombre") {
        val hilo = Hilo(
            nombre = "Hilo de discusión",
            idTema = generateId()
        )

        hilo.idHilo shouldNotBe null
        hilo.nombre shouldBe "Hilo de discusión"
        hilo.idTema shouldNotBe null
    }

    test("crear hilo sin nombre") {
        val hilo = Hilo(
            idTema = generateId()
        )

        hilo.idHilo shouldNotBe null
        hilo.nombre shouldBe ""
    }

    test("crear tema con datos válidos") {
        val tema = Tema(
            nombre = "Tema de prueba"
        )

        tema.idTema shouldNotBe null
        tema.nombre shouldBe "Tema de prueba"
    }

    test("validar IDs únicos") {
        val tema1 = Tema(nombre = "Tema 1")
        val tema2 = Tema(nombre = "Tema 2")

        tema1.idTema shouldNotBe tema2.idTema
    }

    test("validar generación de IDs en hilo") {
        val hilo1 = Hilo(nombre = "Hilo 1")
        val hilo2 = Hilo(nombre = "Hilo 2")

        hilo1.idHilo shouldNotBe hilo2.idHilo
    }
})

