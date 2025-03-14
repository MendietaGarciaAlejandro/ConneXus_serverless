// Datos.kt
package org.connexuss.project.datos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.usuario.AlmacenamientoUsuario
import org.connexuss.project.usuario.UtilidadesUsuario
import org.connexuss.project.usuario.Usuario

val UsuariosPreCreados: SnapshotStateList<Usuario> = run {
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
        almacenamientoUsuario.agregarUsuario(user4)
        almacenamientoUsuario.agregarUsuario(user5)
        almacenamientoUsuario.agregarUsuario(user6)
        almacenamientoUsuario.agregarUsuario(user7)
        almacenamientoUsuario.agregarUsuario(user8)
        almacenamientoUsuario.agregarUsuario(user9)
        almacenamientoUsuario.agregarUsuario(user10)
        almacenamientoUsuario.agregarUsuario(user11)
        almacenamientoUsuario.agregarUsuario(user12)
        almacenamientoUsuario.agregarUsuario(user13)
        almacenamientoUsuario.agregarUsuario(user14)
        almacenamientoUsuario.agregarUsuario(user15)
        almacenamientoUsuario.agregarUsuario(user16)
        almacenamientoUsuario.agregarUsuario(user17)
        almacenamientoUsuario.agregarUsuario(user18)
        almacenamientoUsuario.agregarUsuario(user19)
        almacenamientoUsuario.agregarUsuario(user20)
        almacenamientoUsuario.agregarUsuario(user21)
        almacenamientoUsuario.agregarUsuario(user22)
        almacenamientoUsuario.agregarUsuario(user23)
        almacenamientoUsuario.agregarUsuario(user24)
        almacenamientoUsuario.agregarUsuario(user25)
        almacenamientoUsuario.agregarUsuario(user26)
        almacenamientoUsuario.agregarUsuario(user27)
        almacenamientoUsuario.agregarUsuario(user28)
        almacenamientoUsuario.agregarUsuario(user29)
        almacenamientoUsuario.agregarUsuario(user30)
        almacenamientoUsuario.agregarUsuario(user31)
        almacenamientoUsuario.agregarUsuario(user32)
        almacenamientoUsuario.agregarUsuario(user33)
        almacenamientoUsuario.agregarUsuario(user34)
        almacenamientoUsuario.agregarUsuario(user35)
        almacenamientoUsuario.agregarUsuario(user36)
        almacenamientoUsuario.agregarUsuario(user37)
        almacenamientoUsuario.agregarUsuario(user38)
        almacenamientoUsuario.agregarUsuario(user39)
        almacenamientoUsuario.agregarUsuario(user40)
        almacenamientoUsuario.agregarUsuario(user41)
        almacenamientoUsuario.agregarUsuario(user42)
        almacenamientoUsuario.agregarUsuario(user43)
        almacenamientoUsuario.agregarUsuario(user44)
        almacenamientoUsuario.agregarUsuario(user45)
        almacenamientoUsuario.agregarUsuario(user46)
        almacenamientoUsuario.agregarUsuario(user47)
        almacenamientoUsuario.agregarUsuario(user48)
        almacenamientoUsuario.agregarUsuario(user49)
        almacenamientoUsuario.agregarUsuario(user50)

        usuarios.addAll(almacenamientoUsuario.obtenerUsuarios())
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }
    usuarios
}

//Conversaciones
private val conversacionesIndividuales: List<Conversacion> = listOf(
    Conversacion(
        id = "conv_ind_1",
        participants = listOf("UsuarioPrincipal", "Contacto1"),
        messages = listOf(
            Mensaje(
                id = "msg_ind_1_1",
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto1",
                content = "Hola, ¿cómo estás?",
                fechaMensaje = LocalDateTime(2023, 1, 1, 12, 0)
            ),
            Mensaje(
                id = "msg_ind_1_2",
                senderId = "Contacto1",
                receiverId = "UsuarioPrincipal",
                content = "Muy bien, ¿y tú?",
                fechaMensaje = LocalDateTime(2023, 1, 1, 12, 5)
            )
        )
    ),
    Conversacion(
        id = "conv_ind_2",
        participants = listOf("UsuarioPrincipal", "Contacto2"),
        messages = listOf(
            Mensaje(
                id = "msg_ind_2_1",
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto2",
                content = "¿Nos vemos mañana?",
                fechaMensaje = LocalDateTime(2023, 1, 2, 10, 0)
            ),
            Mensaje(
                id = "msg_ind_2_2",
                senderId = "Contacto2",
                receiverId = "UsuarioPrincipal",
                content = "Claro, ¿a qué hora?",
                fechaMensaje = LocalDateTime(2023, 1, 2, 10, 5)
            )
        )
    ),
    Conversacion(
        id = "conv_ind_3",
        participants = listOf("UsuarioPrincipal", "Contacto3"),
        messages = listOf(
            Mensaje(
                id = "msg_ind_3_1",
                senderId = "Contacto3",
                receiverId = "UsuarioPrincipal",
                content = "¿Has visto la última película?",
                fechaMensaje = LocalDateTime(2023, 1, 3, 15, 0)
            ),
            Mensaje(
                id = "msg_ind_3_2",
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto3",
                content = "Sí, estuvo genial.",
                fechaMensaje = LocalDateTime(2023, 1, 3, 15, 10)
            )
        )
    ),
    Conversacion(
        id = "conv_ind_4",
        participants = listOf("UsuarioPrincipal", "Contacto4"),
        messages = listOf(
            Mensaje(
                id = "msg_ind_4_1",
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto4",
                content = "¿Quieres ir a cenar?",
                fechaMensaje = LocalDateTime(2023, 1, 4, 18, 0)
            ),
            Mensaje(
                id = "msg_ind_4_2",
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
        id = "conv_grp_1",
        participants = listOf("UsuarioPrincipal", "Contacto5", "Contacto6"),
        messages = listOf(
            Mensaje(
                id = "msg_grp_1_1",
                senderId = "Contacto5",
                receiverId = "UsuarioPrincipal",
                content = "Bienvenidos al grupo de estudio",
                fechaMensaje = LocalDateTime(2023, 1, 5, 9, 0)
            ),
            Mensaje(
                id = "msg_grp_1_2",
                senderId = "Contacto6",
                receiverId = "UsuarioPrincipal",
                content = "¿Quién trae los apuntes?",
                fechaMensaje = LocalDateTime(2023, 1, 5, 9, 10)
            )
        )
    ),
    Conversacion(
        id = "conv_grp_2",
        participants = listOf("UsuarioPrincipal", "Contacto7", "Contacto8"),
        messages = listOf(
            Mensaje(
                id = "msg_grp_2_1",
                senderId = "UsuarioPrincipal",
                receiverId = "Contacto7",
                content = "Reunión de trabajo a las 10",
                fechaMensaje = LocalDateTime(2023, 1, 6, 8, 50)
            ),
            Mensaje(
                id = "msg_grp_2_2",
                senderId = "Contacto8",
                receiverId = "UsuarioPrincipal",
                content = "Confirmado, allí estaré",
                fechaMensaje = LocalDateTime(2023, 1, 6, 9, 0)
            )
        )
    ),
    Conversacion(
        id = "conv_grp_3",
        participants = listOf("UsuarioPrincipal", "Contacto9", "Contacto10"),
        messages = listOf(
            Mensaje(
                id = "msg_grp_3_1",
                senderId = "Contacto9",
                receiverId = "UsuarioPrincipal",
                content = "Chicos, ¿organizamos un encuentro este fin de semana?",
                fechaMensaje = LocalDateTime(2023, 1, 7, 16, 0)
            ),
            Mensaje(
                id = "msg_grp_3_2",
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

val UsuarioPrincipal: Usuario = UtilidadesUsuario().instanciaUsuario(
    nombre = "Usuario Principal",
    correo = "principal@example.com",
    aliasPublico = "UsuarioPrincipal",
    activo = true
).apply {
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