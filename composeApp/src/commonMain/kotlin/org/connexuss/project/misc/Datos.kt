package org.connexuss.project.misc

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.ic_chats
import connexus_serverless.composeapp.generated.resources.ic_email
import connexus_serverless.composeapp.generated.resources.ic_foros
import connexus_serverless.composeapp.generated.resources.ic_person
import connexus_serverless.composeapp.generated.resources.*
import connexus_serverless.composeapp.generated.resources.unblock
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.connexuss.project.usuario.Usuario

// Imagenes App -----------------------------------------------------

val imagenesPerfilPersona = mutableListOf<Imagen>().apply {
    add(Imagen("persona001", Res.drawable.persona001))
    add(Imagen("persona002", Res.drawable.persona002))
    add(Imagen("persona003", Res.drawable.persona003))
    add(Imagen("persona004", Res.drawable.persona004))
    add(Imagen("persona005", Res.drawable.persona005))
    add(Imagen("persona006", Res.drawable.persona006))
    add(Imagen("persona007", Res.drawable.persona007))
    add(Imagen("persona008", Res.drawable.persona008))
    add(Imagen("persona009", Res.drawable.persona009))
    add(Imagen("persona010", Res.drawable.persona010))
    add(Imagen("persona011", Res.drawable.persona011))
    add(Imagen("persona012", Res.drawable.persona012))
    add(Imagen("persona013", Res.drawable.persona013))
    add(Imagen("persona014", Res.drawable.persona014))
    add(Imagen("persona015", Res.drawable.persona015))
    add(Imagen("persona016", Res.drawable.persona016))
    add(Imagen("persona017", Res.drawable.persona017))
    add(Imagen("persona018", Res.drawable.persona018))
    add(Imagen("persona019", Res.drawable.persona019))
    add(Imagen("persona020", Res.drawable.persona020))
    add(Imagen("persona021", Res.drawable.persona021))
    add(Imagen("persona022", Res.drawable.persona022))
    add(Imagen("persona023", Res.drawable.persona023))
    add(Imagen("persona024", Res.drawable.persona024))
    add(Imagen("persona025", Res.drawable.persona025))
    add(Imagen("persona026", Res.drawable.persona026))
    add(Imagen("persona027", Res.drawable.persona027))
    add(Imagen("persona028", Res.drawable.persona028))
    add(Imagen("persona029", Res.drawable.persona029))
    add(Imagen("persona030", Res.drawable.persona030))
}

val imagenesPerfilAbstrasto = mutableListOf<Imagen>().apply {
    add(Imagen("abstracto001", Res.drawable.abstracto001))
    add(Imagen("abstracto002", Res.drawable.abstracto002))
    add(Imagen("abstracto003", Res.drawable.abstracto003))
    add(Imagen("abstracto004", Res.drawable.abstracto004))
    add(Imagen("abstracto005", Res.drawable.abstracto005))
    add(Imagen("abstracto006", Res.drawable.abstracto006))
    add(Imagen("abstracto007", Res.drawable.abstracto007))
    add(Imagen("abstracto008", Res.drawable.abstracto008))
    add(Imagen("abstracto009", Res.drawable.abstracto009))
    add(Imagen("abstracto010", Res.drawable.abstracto010))
    add(Imagen("abstracto011", Res.drawable.abstracto011))
    add(Imagen("abstracto012", Res.drawable.abstracto012))
    add(Imagen("abstracto013", Res.drawable.abstracto013))
    add(Imagen("abstracto014", Res.drawable.abstracto014))
    add(Imagen("abstracto015", Res.drawable.abstracto015))
    add(Imagen("abstracto016", Res.drawable.abstracto016))
    add(Imagen("abstracto017", Res.drawable.abstracto017))
    add(Imagen("abstracto018", Res.drawable.abstracto018))
    add(Imagen("abstracto019", Res.drawable.abstracto019))
    add(Imagen("abstracto020", Res.drawable.abstracto020))
    add(Imagen("abstracto021", Res.drawable.abstracto021))
    add(Imagen("abstracto022", Res.drawable.abstracto022))
    add(Imagen("abstracto023", Res.drawable.abstracto023))
    add(Imagen("abstracto024", Res.drawable.abstracto024))
    add(Imagen("abstracto025", Res.drawable.abstracto025))
    add(Imagen("abstracto026", Res.drawable.abstracto026))
    add(Imagen("abstracto027", Res.drawable.abstracto027))
    add(Imagen("abstracto028", Res.drawable.abstracto028))
    add(Imagen("abstracto029", Res.drawable.abstracto029))
    add(Imagen("abstracto030", Res.drawable.abstracto030))
    add(Imagen("abstracto031", Res.drawable.abstracto031))
    add(Imagen("abstracto032", Res.drawable.abstracto032))
    add(Imagen("abstracto033", Res.drawable.abstracto033))
    add(Imagen("abstracto034", Res.drawable.abstracto034))
    add(Imagen("abstracto035", Res.drawable.abstracto035))
    add(Imagen("abstracto036", Res.drawable.abstracto036))
    add(Imagen("abstracto037", Res.drawable.abstracto037))
    add(Imagen("abstracto038", Res.drawable.abstracto038))
    add(Imagen("abstracto039", Res.drawable.abstracto039))
    add(Imagen("abstracto040", Res.drawable.abstracto040))
    add(Imagen("abstracto041", Res.drawable.abstracto041))
    add(Imagen("abstracto042", Res.drawable.abstracto042))
    add(Imagen("abstracto043", Res.drawable.abstracto043))
    add(Imagen("abstracto044", Res.drawable.abstracto044))
    add(Imagen("abstracto045", Res.drawable.abstracto045))
    add(Imagen("abstracto046", Res.drawable.abstracto046))
    add(Imagen("abstracto047", Res.drawable.abstracto047))
    add(Imagen("abstracto048", Res.drawable.abstracto048))
    add(Imagen("abstracto049", Res.drawable.abstracto049))
    add(Imagen("abstracto050", Res.drawable.abstracto050))
    add(Imagen("abstracto051", Res.drawable.abstracto051))
}

val imagenesPerfilDibujo = mutableListOf<Imagen>().apply {
    add(Imagen("dibujo001", Res.drawable.dibujo001))
    add(Imagen("dibujo002", Res.drawable.dibujo002))
    add(Imagen("dibujo003", Res.drawable.dibujo003))
    add(Imagen("dibujo004", Res.drawable.dibujo004))
    add(Imagen("dibujo005", Res.drawable.dibujo005))
    add(Imagen("dibujo006", Res.drawable.dibujo006))
    add(Imagen("dibujo007", Res.drawable.dibujo007))
    add(Imagen("dibujo008", Res.drawable.dibujo008))
    add(Imagen("dibujo009", Res.drawable.dibujo009))
    add(Imagen("dibujo010", Res.drawable.dibujo010))
    add(Imagen("dibujo011", Res.drawable.dibujo011))
    add(Imagen("dibujo012", Res.drawable.dibujo012))
    add(Imagen("dibujo013", Res.drawable.dibujo013))
    add(Imagen("dibujo014", Res.drawable.dibujo014))
    add(Imagen("dibujo015", Res.drawable.dibujo015))
    add(Imagen("dibujo016", Res.drawable.dibujo016))
    add(Imagen("dibujo017", Res.drawable.dibujo017))
    add(Imagen("dibujo018", Res.drawable.dibujo018))
    add(Imagen("dibujo019", Res.drawable.dibujo019))
    add(Imagen("dibujo020", Res.drawable.dibujo020))
    add(Imagen("dibujo021", Res.drawable.dibujo021))
    add(Imagen("dibujo022", Res.drawable.dibujo022))
    add(Imagen("dibujo023", Res.drawable.dibujo023))
    add(Imagen("dibujo024", Res.drawable.dibujo024))
    add(Imagen("dibujo025", Res.drawable.dibujo025))
    add(Imagen("dibujo026", Res.drawable.dibujo026))
    add(Imagen("dibujo027", Res.drawable.dibujo027))
    add(Imagen("dibujo028", Res.drawable.dibujo028))
    add(Imagen("dibujo029", Res.drawable.dibujo029))
    add(Imagen("dibujo030", Res.drawable.dibujo030))
    add(Imagen("dibujo031", Res.drawable.dibujo031))
    add(Imagen("dibujo032", Res.drawable.dibujo032))
}

val UsuariosPreCreados: SnapshotStateList<Usuario> = run {

    val imagenesApp = mutableListOf<Imagen>()
    imagenesApp.add(Imagen("logo", Res.drawable.connexus))
    imagenesApp.add(Imagen("avatar", Res.drawable.avatar))
    imagenesApp.add(Imagen("unblock", Res.drawable.unblock))
    imagenesApp.add(Imagen("ic_chats", Res.drawable.ic_chats))
    imagenesApp.add(Imagen("ic_email", Res.drawable.ic_email))
    imagenesApp.add(Imagen("ic_foros", Res.drawable.ic_foros))
    imagenesApp.add(Imagen("ic_person", Res.drawable.ic_person))
    imagenesApp.add(Imagen("visibilidadOn", Res.drawable.visibilidadOn))
    imagenesApp.add(Imagen("visibilidadOff", Res.drawable.visibilidadOff))



    val almacenamientoUsuario = AlmacenamientoUsuario()
    val usuarios = mutableStateListOf<Usuario>()
    try {
        // Usuarios iniciales
        val user1 = UtilidadesUsuario().instanciaUsuario(
            "JP",
            "Juan Perez",
            25,
            "paco@jerte.org",
            "pakito58",
            true
        )
        val user2 = UtilidadesUsuario().instanciaUsuario(
            "ML",
            "Maria Lopez",
            30,
            "marii@si.se",
            "marii",
            true
        )
        val user3 = UtilidadesUsuario().instanciaUsuario(
            "PS",
            "Pedro Sanchez",
            40,
            "roba@espannoles.es",
            "roba",
            true
        )
        // Agregamos estos primeros usuarios al almacenamiento
        almacenamientoUsuario.agregarUsuario(user1)
        almacenamientoUsuario.agregarUsuario(user2)
        almacenamientoUsuario.agregarUsuario(user3)

        // Usuarios adicionales
        val user4 = UtilidadesUsuario().instanciaUsuario(
            "Carla Montes",
            "carla.montes@example.com",
            "carlam27",
            true
        )
        val user5 = UtilidadesUsuario().instanciaUsuario(
            "Sofía Hernández",
            "sofia.hernandez@example.com",
            "sofia32",
            true
        )
        val user6 = UtilidadesUsuario().instanciaUsuario(
            "Pablo Ortiz",
            "pablo.ortiz@example.com",
            "pablo29",
            true
        )
        val user7 = UtilidadesUsuario().instanciaUsuario(
            "Lucía Ramos",
            "lucia.ramos@example.com",
            "luciar22",
            true
        )
        val user8 = UtilidadesUsuario().instanciaUsuario(
            "Sergio Blanco",
            "sergio.blanco@example.com",
            "sergiob45",
            true
        )
        val user9 = UtilidadesUsuario().instanciaUsuario(
            "Andrea Alarcón",
            "andrea.alarcon@example.com",
            "andrea35",
            true
        )
        val user10 = UtilidadesUsuario().instanciaUsuario(
            "Miguel Flores",
            "miguel.flores@example.com",
            "miguelf19",
            true
        )
        val user11 = UtilidadesUsuario().instanciaUsuario(
            "Sara González",
            "sara.gonzalez@example.com",
            "sarag31",
            true
        )
        val user12 = UtilidadesUsuario().instanciaUsuario(
            "David Medina",
            "david.medina@example.com",
            "davidm28",
            true
        )
        val user13 = UtilidadesUsuario().instanciaUsuario(
            "Elena Ruiz",
            "elena.ruiz@example.com",
            "elena26",
            true
        )
        val user14 = UtilidadesUsuario().instanciaUsuario(
            "Alberto Vega",
            "alberto.vega@example.com",
            "albertov43",
            true
        )
        val user15 = UtilidadesUsuario().instanciaUsuario(
            "Julia Rojas",
            "julia.rojas@example.com",
            "juliar37",
            true
        )
        val user16 = UtilidadesUsuario().instanciaUsuario(
            "Marcos Fernández",
            "marcos.fernandez@example.com",
            "marcos41",
            true
        )
        val user17 = UtilidadesUsuario().instanciaUsuario(
            "Daniela Muñoz",
            "daniela.munoz@example.com",
            "danimu20",
            true
        )
        val user18 = UtilidadesUsuario().instanciaUsuario(
            "Carlos Pérez",
            "carlos.perez@example.com",
            "carlosp34",
            true
        )
        val user19 = UtilidadesUsuario().instanciaUsuario(
            "Tamara Díaz",
            "tamara.diaz@example.com",
            "tamarad38",
            true
        )
        val user20 = UtilidadesUsuario().instanciaUsuario(
            "Gonzalo Márquez",
            "gonzalo.marquez@example.com",
            "gonzalom24",
            true
        )
        val user21 = UtilidadesUsuario().instanciaUsuario(
            "Patricia Soto",
            "patricia.soto@example.com",
            "patricias36",
            true
        )
        val user22 = UtilidadesUsuario().instanciaUsuario(
            "Raúl Campos",
            "raul.campos@example.com",
            "raulc42",
            true
        )
        val user23 = UtilidadesUsuario().instanciaUsuario(
            "Irene Cabrera",
            "irene.cabrera@example.com",
            "irene25",
            true
        )
        val user24 = UtilidadesUsuario().instanciaUsuario(
            "Rodrigo Luna",
            "rodrigo.luna@example.com",
            "rodrigo33",
            true
        )
        val user25 = UtilidadesUsuario().instanciaUsuario(
            "Nerea Delgado",
            "nerea.delgado@example.com",
            "neread29",
            true
        )
        val user26 = UtilidadesUsuario().instanciaUsuario(
            "Adrián Ramos",
            "adrian.ramos@example.com",
            "adrianr44",
            true
        )
        val user27 = UtilidadesUsuario().instanciaUsuario(
            "Beatriz Calderón",
            "beatriz.calderon@example.com",
            "beac27",
            true
        )
        val user28 = UtilidadesUsuario().instanciaUsuario(
            "Ximena Navarro",
            "ximena.navarro@example.com",
            "ximenan23",
            true
        )
        val user29 = UtilidadesUsuario().instanciaUsuario(
            "Felipe Lara",
            "felipe.lara@example.com",
            "felipel39",
            true
        )
        val user30 = UtilidadesUsuario().instanciaUsuario(
            "Olga Martín",
            "olga.martin@example.com",
            "olgam21",
            true
        )
        val user31 = UtilidadesUsuario().instanciaUsuario(
            "Diego Castillo",
            "diego.castillo@example.com",
            "diegoc48",
            true
        )
        val user32 = UtilidadesUsuario().instanciaUsuario(
            "Alicia León",
            "alicia.leon@example.com",
            "alicia26",
            true
        )
        val user33 = UtilidadesUsuario().instanciaUsuario(
            "Tomás Vázquez",
            "tomas.vazquez@example.com",
            "tomasv54",
            true
        )
        val user34 = UtilidadesUsuario().instanciaUsuario(
            "Natalia Espinosa",
            "natalia.espinosa@example.com",
            "nataliae29",
            true
        )
        val user35 = UtilidadesUsuario().instanciaUsuario(
            "Esteban Gil",
            "esteban.gil@example.com",
            "estebang51",
            true
        )
        val user36 = UtilidadesUsuario().instanciaUsuario(
            "Rosa Ibáñez",
            "rosa.ibanez@example.com",
            "rosai30",
            true
        )
        val user37 = UtilidadesUsuario().instanciaUsuario(
            "Luis Morales",
            "luis.morales@example.com",
            "luism55",
            true
        )
        val user38 = UtilidadesUsuario().instanciaUsuario(
            "Diana Acosta",
            "diana.acosta@example.com",
            "dianaa33",
            true
        )
        val user39 = UtilidadesUsuario().instanciaUsuario(
            "Javier Ponce",
            "javier.ponce@example.com",
            "javiers46",
            true
        )
        val user40 = UtilidadesUsuario().instanciaUsuario(
            "Carmen Rivero",
            "carmen.rivero@example.com",
            "carmen28",
            true
        )
        val user41 = UtilidadesUsuario().instanciaUsuario(
            "Andrés Silva",
            "andres.silva@example.com",
            "andress47",
            true
        )
        val user42 = UtilidadesUsuario().instanciaUsuario(
            "Laura Sancho",
            "laura.sancho@example.com",
            "lauras24",
            true
        )
        val user43 = UtilidadesUsuario().instanciaUsuario(
            "Gabriel Escudero",
            "gabriel.escudero@example.com",
            "gabriele31",
            true
        )
        val user44 = UtilidadesUsuario().instanciaUsuario(
            "Dario Esparza",
            "dario.esparza@example.com",
            "darioe36",
            true
        )
        val user45 = UtilidadesUsuario().instanciaUsuario(
            "Verónica Salazar",
            "veronica.salazar@example.com",
            "veronicas25",
            true
        )
        val user46 = UtilidadesUsuario().instanciaUsuario(
            "Ernesto Herrera",
            "ernesto.herrera@example.com",
            "ernestoh34",
            true
        )
        val user47 = UtilidadesUsuario().instanciaUsuario(
            "Isabel Fuentes",
            "isabel.fuentes@example.com",
            "isabelf26",
            true
        )
        val user48 = UtilidadesUsuario().instanciaUsuario(
            "Matías Rosales",
            "matias.rosales@example.com",
            "matiasr38",
            true
        )
        val user49 = UtilidadesUsuario().instanciaUsuario(
            "Lorena Dávila",
            "lorena.davila@example.com",
            "lorenad23",
            true
        )
        val user50 = UtilidadesUsuario().instanciaUsuario(
            "Santiago Ibarra",
            "santiago.ibarra@example.com",
            "santi44",
            true
        )

        // Agregamos los usuarios adicionales al almacenamiento
        if (user13 != null) {
            almacenamientoUsuario.agregarUsuario(user13)
        }
        if (user14 != null) {
            almacenamientoUsuario.agregarUsuario(user14)
        }
        if (user15 != null) {
            almacenamientoUsuario.agregarUsuario(user15)
        }
        if (user16 != null) {
            almacenamientoUsuario.agregarUsuario(user16)
        }
        if (user17 != null) {
            almacenamientoUsuario.agregarUsuario(user17)
        }
        if (user18 != null) {
            almacenamientoUsuario.agregarUsuario(user18)
        }
        if (user19 != null) {
            almacenamientoUsuario.agregarUsuario(user19)
        }
        if (user20 != null) {
            almacenamientoUsuario.agregarUsuario(user20)
        }
        if (user21 != null) {
            almacenamientoUsuario.agregarUsuario(user21)
        }
        if (user22 != null) {
            almacenamientoUsuario.agregarUsuario(user22)
        }
        if (user23 != null) {
            almacenamientoUsuario.agregarUsuario(user23)
        }
        if (user24 != null) {
            almacenamientoUsuario.agregarUsuario(user24)
        }
        if (user25 != null) {
            almacenamientoUsuario.agregarUsuario(user25)
        }
        if (user26 != null) {
            almacenamientoUsuario.agregarUsuario(user26)
        }
        if (user27 != null) {
            almacenamientoUsuario.agregarUsuario(user27)
        }
        if (user28 != null) {
            almacenamientoUsuario.agregarUsuario(user28)
        }
        if (user29 != null) {
            almacenamientoUsuario.agregarUsuario(user29)
        }
        if (user30 != null) {
            almacenamientoUsuario.agregarUsuario(user30)
        }
        if (user31 != null) {
            almacenamientoUsuario.agregarUsuario(user31)
        }
        if (user32 != null) {
            almacenamientoUsuario.agregarUsuario(user32)
        }
        if (user33 != null) {
            almacenamientoUsuario.agregarUsuario(user33)
        }
        if (user34 != null) {
            almacenamientoUsuario.agregarUsuario(user34)
        }
        if (user35 != null) {
            almacenamientoUsuario.agregarUsuario(user35)
        }
        if (user36 != null) {
            almacenamientoUsuario.agregarUsuario(user36)
        }
        if (user37 != null) {
            almacenamientoUsuario.agregarUsuario(user37)
        }
        if (user38 != null) {
            almacenamientoUsuario.agregarUsuario(user38)
        }
        if (user39 != null) {
            almacenamientoUsuario.agregarUsuario(user39)
        }
        if (user40 != null) {
            almacenamientoUsuario.agregarUsuario(user40)
        }
        if (user41 != null) {
            almacenamientoUsuario.agregarUsuario(user41)
        }
        if (user42 != null) {
            almacenamientoUsuario.agregarUsuario(user42)
        }
        if (user43 != null) {
            almacenamientoUsuario.agregarUsuario(user43)
        }
        if (user44 != null) {
            almacenamientoUsuario.agregarUsuario(user44)
        }
        if (user45 != null) {
            almacenamientoUsuario.agregarUsuario(user45)
        }
        if (user46 != null) {
            almacenamientoUsuario.agregarUsuario(user46)
        }
        if (user47 != null) {
            almacenamientoUsuario.agregarUsuario(user47)
        }
        if (user48 != null) {
            almacenamientoUsuario.agregarUsuario(user48)
        }
        if (user49 != null) {
            almacenamientoUsuario.agregarUsuario(user49)
        }
        if (user50 != null) {
            almacenamientoUsuario.agregarUsuario(user50)
        }

        usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }
    usuarios
}

//Conversaciones
private val conversacionesIndividuales: List<Conversacion> = listOf(
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto1"),
        messages = listOf(
            Mensaje(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto1",
                content = "Hola, ¿cómo estás?",
                fechaMensaje = LocalDateTime(2023, 1, 1, 12, 0)
            ),
            Mensaje(
                senderId = "Contacto1",
                receiverId = "UsuarioPrincipal",
                content = "Muy bien, ¿y tú?",
                fechaMensaje = LocalDateTime(2023, 1, 1, 12, 5)
            )
        )
    ),
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto2"),
        messages = listOf(
            Mensaje(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto2",
                content = "¿Nos vemos mañana?",
                fechaMensaje = LocalDateTime(2023, 1, 2, 10, 0)
            ),
            Mensaje(
                senderId = "Contacto2",
                receiverId = "UsuarioPrincipal",
                content = "Claro, ¿a qué hora?",
                fechaMensaje = LocalDateTime(2023, 1, 2, 10, 5)
            )
        )
    ),
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto3"),
        messages = listOf(
            Mensaje(
                senderId = "Contacto3",
                receiverId = "UsuarioPrincipal",
                content = "¿Has visto la última película?",
                fechaMensaje = LocalDateTime(2023, 1, 3, 15, 0)
            ),
            Mensaje(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto3",
                content = "Sí, estuvo genial.",
                fechaMensaje = LocalDateTime(2023, 1, 3, 15, 10)
            )
        )
    ),
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto4"),
        messages = listOf(
            Mensaje(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto4",
                content = "¿Quieres ir a cenar?",
                fechaMensaje = LocalDateTime(2023, 1, 4, 18, 0)
            ),
            Mensaje(
                senderId = "Contacto4",
                receiverId = "UsuarioPrincipal",
                content = "¡Claro, suena perfecto!",
                fechaMensaje = LocalDateTime(2023, 1, 4, 18, 15)
            )
        )
    )
)

// Conversaciones de grupo (3)
private val conversacionesGrupo: List<Conversacion> = listOf(
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto5", "Contacto6"),
        messages = listOf(
            Mensaje(
                senderId = "Contacto5",
                receiverId = "UsuarioPrincipal",
                content = "Bienvenidos al grupo de estudio",
                fechaMensaje = LocalDateTime(2023, 1, 5, 9, 0)
            ),
            Mensaje(
                senderId = "Contacto6",
                receiverId = "UsuarioPrincipal",
                content = "¿Quién trae los apuntes?",
                fechaMensaje = LocalDateTime(2023, 1, 5, 9, 10)
            )
        )
    ),
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto7", "Contacto8"),
        messages = listOf(
            Mensaje(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto7",
                content = "Reunión de trabajo a las 10",
                fechaMensaje = LocalDateTime(2023, 1, 6, 8, 50)
            ),
            Mensaje(
                senderId = "Contacto8",
                receiverId = "UsuarioPrincipal",
                content = "Confirmado, allí estaré",
                fechaMensaje = LocalDateTime(2023, 1, 6, 9, 0)
            )
        )
    ),
    Conversacion(
        participants = listOf("UsuarioPrincipal", "Contacto9", "Contacto10"),
        messages = listOf(
            Mensaje(
                senderId = "Contacto9",
                receiverId = "UsuarioPrincipal",
                content = "Chicos, ¿organizamos un encuentro este fin de semana?",
                fechaMensaje = LocalDateTime(2023, 1, 7, 16, 0)
            ),
            Mensaje(
                senderId = "Contacto10",
                receiverId = "UsuarioPrincipal",
                content = "Me apunto, cuenten conmigo.",
                fechaMensaje = LocalDateTime(2023, 1, 7, 16, 10)
            )
        )
    )
)

val conversacionesPreInicializadasUsuarioPrincipal: List<Conversacion> =
    conversacionesIndividuales + conversacionesGrupo

var UsuarioPrincipal: Usuario? = UtilidadesUsuario().instanciaUsuario(
    nombre = "Usuario Principal",
    correo = "principal@example.com",
    aliasPublico = "UsuarioPrincipal",
    activo = true
)?.apply {
    // Asigna algunos contactos de ejemplo (puedes modificar según tus necesidades)
    setContactos(listOf("JP", "ML"))
    // Define un chat de ejemplo para este usuario
    setChatUser(
        ConversacionesUsuario(
            id = "chatUser_1",
            idUser = "UsuarioPrincipal",
            conversaciones = conversacionesPreInicializadasUsuarioPrincipal
        )
    )
}

// Datos foro

val hilosForo: List<Hilo> = listOf(
    Hilo(
        idForeros = listOf("UsuarioPrincipal", "Contacto1"),
        posts = listOf(
            Post(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto1",
                content = "Hola, ¿cómo estás?",
                fechaPost = LocalDateTime(2023, 1, 1, 12, 0)
            ),
            Post(
                senderId = "Contacto1",
                receiverId = "UsuarioPrincipal",
                content = "Muy bien, ¿y tú?",
                fechaPost = LocalDateTime(2023, 1, 1, 12, 5)
            )
        ),
        nombre = "Hilo de conversación 1"
    ),
    Hilo(
        idHilo = "hilo_2",
        idForeros = listOf("UsuarioPrincipal", "Contacto2"),
        posts = listOf(
            Post(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto2",
                content = "¿Nos vemos mañana?",
                fechaPost = LocalDateTime(2023, 1, 2, 10, 0)
            ),
            Post(
                senderId = "Contacto2",
                receiverId = "UsuarioPrincipal",
                content = "Claro, ¿a qué hora?",
                fechaPost = LocalDateTime(2023, 1, 2, 10, 5)
            )
        ),
        nombre = "Hilo de conversación 2"
    ),
    Hilo(
        idHilo = "hilo_3",
        idForeros = listOf("UsuarioPrincipal", "Contacto3"),
        posts = listOf(
            Post(
                senderId = "Contacto3",
                receiverId = "UsuarioPrincipal",
                content = "¿Has visto la última película?",
                fechaPost = LocalDateTime(2023, 1, 3, 15, 0)
            ),
            Post(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto3",
                content = "Sí, estuvo genial.",
                fechaPost = LocalDateTime(2023, 1, 3, 15, 10)
            )
        ),
        nombre = "Hilo de conversación 3"
    ),
    Hilo(
        idHilo = "hilo_4",
        idForeros = listOf("UsuarioPrincipal", "Contacto4"),
        posts = listOf(
            Post(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto4",
                content = "¿Quieres ir a cenar?",
                fechaPost = LocalDateTime(2023, 1, 4, 18, 0)
            ),
            Post(
                senderId = "Contacto4",
                receiverId = "UsuarioPrincipal",
                content = "¡Claro, suena perfecto!",
                fechaPost = LocalDateTime(2023, 1, 4, 18, 15)
            )
        ),
        nombre = "Hilo de conversación 4"
    ),
    Hilo(
        idHilo = "hilo_5",
        idForeros = listOf("UsuarioPrincipal", "Contacto5", "Contacto6"),
        posts = listOf(
            Post(
                senderId = "Contacto5",
                receiverId = "UsuarioPrincipal",
                content = "Bienvenidos al grupo de estudio",
                fechaPost = LocalDateTime(2023, 1, 5, 9, 0)
            ),
            Post(
                senderId = "Contacto6",
                receiverId = "UsuarioPrincipal",
                content = "¿Quién trae los apuntes?",
                fechaPost = LocalDateTime(2023, 1, 5, 9, 10)
            )
        ),
        nombre = "Hilo de grupo 1"
    ),
    Hilo(
        idHilo = "hilo_6",
        idForeros = listOf("UsuarioPrincipal", "Contacto7", "Contacto8"),
        posts = listOf(
            Post(
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto7",
                content = "Reunión de trabajo a las 10",
                fechaPost = LocalDateTime(2023, 1, 6, 8, 50)
            ),
            Post(
                senderId = "Contacto8",
                receiverId = "UsuarioPrincipal",
                content = "Confirmado, allí estaré",
                fechaPost = LocalDateTime(2023, 1, 6, 9, 0)
            )
        ),
        nombre = "Hilo de grupo 2"
    ),
    Hilo(
        idHilo = "hilo_7",
        idForeros = listOf("UsuarioPrincipal", "Contacto9", "Contacto10"),
        posts = listOf(
            Post(
                senderId = "Contacto9",
                receiverId = "UsuarioPrincipal",
                content = "Chicos, ¿organizamos un encuentro este fin de semana?",
                fechaPost = LocalDateTime(2023, 1, 7, 16, 0)
            ),
            Post(
                senderId = "Contacto10",
                receiverId = "UsuarioPrincipal",
                content = "Me apunto, cuenten conmigo.",
                fechaPost = LocalDateTime(2023, 1, 7, 16, 10)
            )
        ),
        nombre = "Hilo de grupo 3"
    )
)

val temasForo: List<Tema> = listOf(
    Tema(
        idTema = "tema_1",
        hilos = listOf(hilosForo[0], hilosForo[1]),
        nombre = "Tema de Foro 1",
        idUsuario = "UsuarioPrincipal"
    ),
    Tema(
        idTema = "tema_2",
        hilos = listOf(hilosForo[2], hilosForo[3]),
        nombre = "Tema de Foro 2",
        idUsuario = "UsuarioPrincipal"
    ),
    Tema(
        idTema = "tema_3",
        hilos = listOf(hilosForo[4], hilosForo[5]),
        nombre = "Tema de Foro 3",
        idUsuario = "UsuarioPrincipal"
    )
)

// Nuevos datos del foro --------------------------------------------



// Lista temporal de Temas + Hilos + Posts
val temasHilosPosts = listOf(
    Tema(
        idTema = "Tema 1",
        idUsuario = "usuario1", // ID de usuario (puedes generar o asignar un valor específico)
        nombre = "Tema 1",
        hilos = listOf(
            Hilo(
                idHilo = "Hilo 1",
                idForeros = listOf("usuario1", "usuario2"),
                posts = listOf(
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario2",
                        content = "Post 1 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario1",
                        content = "Post 2 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario2",
                        content = "Post 3 en Hilo 1"
                    )
                ),
                nombre = "Hilo 1"
            ),
            Hilo(
                idHilo = "Hilo 2",
                idForeros = listOf("usuario1", "usuario3"),
                posts = listOf(
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario3",
                        content = "Post 1 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario1",
                        content = "Post 2 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario3",
                        content = "Post 3 en Hilo 2"
                    )
                ),
                nombre = "Hilo 2"
            ),
            Hilo(
                idHilo = "Hilo 3",
                idForeros = listOf("usuario1", "usuario4"),
                posts = listOf(
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario4",
                        content = "Post 1 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario4",
                        receiverId = "usuario1",
                        content = "Post 2 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario1",
                        receiverId = "usuario4",
                        content = "Post 3 en Hilo 3"
                    )
                ),
                nombre = "Hilo 3"
            )
        )
    ),
    Tema(
        idTema = "Tema 2",
        idUsuario = "usuario2",
        nombre = "Tema 2",
        hilos = listOf(
            Hilo(
                idHilo = "Hilo 1",
                idForeros = listOf("usuario2", "usuario5"),
                posts = listOf(
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario5",
                        content = "Post 1 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario5",
                        receiverId = "usuario2",
                        content = "Post 2 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario5",
                        content = "Post 3 en Hilo 1"
                    )
                ),
                nombre = "Hilo 1"
            ),
            Hilo(
                idHilo = "Hilo 2",
                idForeros = listOf("usuario2", "usuario6"),
                posts = listOf(
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario6",
                        content = "Post 1 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario6",
                        receiverId = "usuario2",
                        content = "Post 2 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario6",
                        content = "Post 3 en Hilo 2"
                    )
                ),
                nombre = "Hilo 2"
            ),
            Hilo(
                idHilo = "Hilo 3",
                idForeros = listOf("usuario2", "usuario7"),
                posts = listOf(
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario7",
                        content = "Post 1 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario7",
                        receiverId = "usuario2",
                        content = "Post 2 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario2",
                        receiverId = "usuario7",
                        content = "Post 3 en Hilo 3"
                    )
                ),
                nombre = "Hilo 3"
            )
        )
    ),
    Tema(
        idTema = "Tema 3",
        idUsuario = "usuario3",
        nombre = "Tema 3",
        hilos = listOf(
            Hilo(
                idHilo = "Hilo 1",
                idForeros = listOf("usuario3", "usuario8"),
                posts = listOf(
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario8",
                        content = "Post 1 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario8",
                        receiverId = "usuario3",
                        content = "Post 2 en Hilo 1"
                    ),
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario8",
                        content = "Post 3 en Hilo 1"
                    )
                ),
                nombre = "Hilo 1"
            ),
            Hilo(
                idHilo = "Hilo 2",
                idForeros = listOf("usuario3", "usuario9"),
                posts = listOf(
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario9",
                        content = "Post 1 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario9",
                        receiverId = "usuario3",
                        content = "Post 2 en Hilo 2"
                    ),
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario9",
                        content = "Post 3 en Hilo 2"
                    )
                ),
                nombre = "Hilo 2"
            ),
            Hilo(
                idHilo = "Hilo 3",
                idForeros = listOf("usuario3", "usuario10"),
                posts = listOf(
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario10",
                        content = "Post 1 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario10",
                        receiverId = "usuario3",
                        content = "Post 2 en Hilo 3"
                    ),
                    Post(
                        senderId = "usuario3",
                        receiverId = "usuario10",
                        content = "Post 3 en Hilo 3"
                    )
                ),
                nombre = "Hilo 3"
            )
        )
    )
)

/**
 * Objeto repositorio para gestionar los temas del foro.
 */
object ForoRepository {
    // Lista de temas como estado mutable global, inicializada vacía
    val temas = mutableStateListOf<Tema>().apply {
        addAll(temasHilosPosts)
    }

    /**
     * Agrega un nuevo tema al repositorio.
     *
     * @param nuevoTema El tema a agregar.
     */
    fun agregarTema(nuevoTema: Tema) {
        temas.add(nuevoTema)
    }

    /**
     * Actualiza un tema existente en el repositorio.
     *
     * @param temaActualizado El tema con la información actualizada.
     */
    fun actualizarTema(temaActualizado: Tema) {
        temas.indexOfFirst { it.idTema == temaActualizado.idTema }
            .takeIf { it != -1 }
            ?.let { index -> temas[index] = temaActualizado }
    }

    /**
     * Obtiene un tema en función de su identificador.
     *
     * @param idTema El identificador del tema.
     * @return El tema correspondiente o null si no se encuentra.
     */
    fun obtenerTemaPorId(idTema: String): Tema? =
        temas.find { it.idTema == idTema }

    /**
     * Actualiza un hilo dentro de un tema.
     *
     * @param temaId El identificador del tema.
     * @param hiloActualizado El hilo con los datos actualizados.
     */
    fun actualizarHiloEnTema(temaId: String, hiloActualizado: Hilo) {
        obtenerTemaPorId(temaId)?.let { tema ->
            val nuevosHilos = tema.hilos.map {
                if (it.idHilo == hiloActualizado.idHilo) hiloActualizado else it
            }
            actualizarTema(tema.copy(hilos = nuevosHilos))
        }
    }

    /**
     * Actualiza un post dentro de un hilo en un tema.
     *
     * @param temaId El identificador del tema.
     * @param hiloId El identificador del hilo.
     * @param postActualizado El post con los datos actualizados.
     */
    fun actualizarPostEnHilo(temaId: String, hiloId: String, postActualizado: Post) {
        obtenerTemaPorId(temaId)?.let { tema ->
            val nuevosHilos = tema.hilos.map { hilo ->
                if (hilo.idHilo == hiloId) {
                    val nuevosPosts = hilo.posts.map { post ->
                        if (post.idPost == postActualizado.idPost) postActualizado else post
                    }
                    hilo.copy(posts = nuevosPosts)
                } else hilo
            }
            actualizarTema(tema.copy(hilos = nuevosHilos))
        }
    }
}
/**
 * Objeto repositorio para gestionar los hilos del foro.
 */
object HilosRepository {
    // Lista mutable de hilos
    val hilos = mutableStateListOf<Hilo>()

    /**
     * Agrega un nuevo hilo a la lista.
     *
     * @param nuevoHilo El hilo a agregar.
     */
    fun agregarHilo(nuevoHilo: Hilo) {
        hilos.add(nuevoHilo)
    }

    /**
     * Actualiza un hilo existente en la lista.
     *
     * @param hiloActualizado El hilo con los datos actualizados.
     */
    fun actualizarHilo(hiloActualizado: Hilo) {
        hilos.indexOfFirst { it.idHilo == hiloActualizado.idHilo }
            .takeIf { it != -1 }
            ?.let { index -> hilos[index] = hiloActualizado }
    }
}
/**
 * Objeto repositorio para gestionar los posts del foro.
 */
object PostsRepository {
    // Lista global mutable de posts
    val posts = mutableStateListOf<Post>()

    /**
     * Agrega un nuevo post a la lista.
     *
     * @param nuevoPost El post a agregar.
     */
    fun agregarPost(nuevoPost: Post) {
        posts.add(nuevoPost)
    }

    /**
     * Actualiza un post existente en la lista.
     *
     * @param postActualizado El post con los datos actualizados.
     */
    fun actualizarPost(postActualizado: Post) {
        posts.indexOfFirst { it.idPost == postActualizado.idPost }
            .takeIf { it != -1 }
            ?.let { index -> posts[index] = postActualizado }
    }
}