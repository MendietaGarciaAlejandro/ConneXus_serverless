package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.abstracto001
import connexus_serverless.composeapp.generated.resources.abstracto002
import connexus_serverless.composeapp.generated.resources.abstracto003
import connexus_serverless.composeapp.generated.resources.abstracto004
import connexus_serverless.composeapp.generated.resources.abstracto005
import connexus_serverless.composeapp.generated.resources.abstracto006
import connexus_serverless.composeapp.generated.resources.abstracto007
import connexus_serverless.composeapp.generated.resources.abstracto008
import connexus_serverless.composeapp.generated.resources.abstracto009
import connexus_serverless.composeapp.generated.resources.abstracto010
import connexus_serverless.composeapp.generated.resources.abstracto011
import connexus_serverless.composeapp.generated.resources.abstracto012
import connexus_serverless.composeapp.generated.resources.abstracto013
import connexus_serverless.composeapp.generated.resources.abstracto014
import connexus_serverless.composeapp.generated.resources.abstracto015
import connexus_serverless.composeapp.generated.resources.abstracto016
import connexus_serverless.composeapp.generated.resources.abstracto017
import connexus_serverless.composeapp.generated.resources.abstracto018
import connexus_serverless.composeapp.generated.resources.abstracto019
import connexus_serverless.composeapp.generated.resources.abstracto020
import connexus_serverless.composeapp.generated.resources.abstracto021
import connexus_serverless.composeapp.generated.resources.abstracto022
import connexus_serverless.composeapp.generated.resources.abstracto023
import connexus_serverless.composeapp.generated.resources.abstracto024
import connexus_serverless.composeapp.generated.resources.abstracto025
import connexus_serverless.composeapp.generated.resources.abstracto026
import connexus_serverless.composeapp.generated.resources.abstracto027
import connexus_serverless.composeapp.generated.resources.abstracto028
import connexus_serverless.composeapp.generated.resources.abstracto029
import connexus_serverless.composeapp.generated.resources.abstracto030
import connexus_serverless.composeapp.generated.resources.abstracto031
import connexus_serverless.composeapp.generated.resources.abstracto032
import connexus_serverless.composeapp.generated.resources.abstracto033
import connexus_serverless.composeapp.generated.resources.abstracto034
import connexus_serverless.composeapp.generated.resources.abstracto035
import connexus_serverless.composeapp.generated.resources.abstracto036
import connexus_serverless.composeapp.generated.resources.abstracto037
import connexus_serverless.composeapp.generated.resources.abstracto038
import connexus_serverless.composeapp.generated.resources.abstracto039
import connexus_serverless.composeapp.generated.resources.abstracto040
import connexus_serverless.composeapp.generated.resources.abstracto041
import connexus_serverless.composeapp.generated.resources.abstracto042
import connexus_serverless.composeapp.generated.resources.abstracto043
import connexus_serverless.composeapp.generated.resources.abstracto044
import connexus_serverless.composeapp.generated.resources.abstracto045
import connexus_serverless.composeapp.generated.resources.abstracto046
import connexus_serverless.composeapp.generated.resources.abstracto047
import connexus_serverless.composeapp.generated.resources.abstracto048
import connexus_serverless.composeapp.generated.resources.abstracto049
import connexus_serverless.composeapp.generated.resources.abstracto050
import connexus_serverless.composeapp.generated.resources.abstracto051
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.dibujo001
import connexus_serverless.composeapp.generated.resources.dibujo002
import connexus_serverless.composeapp.generated.resources.dibujo003
import connexus_serverless.composeapp.generated.resources.dibujo004
import connexus_serverless.composeapp.generated.resources.dibujo005
import connexus_serverless.composeapp.generated.resources.dibujo006
import connexus_serverless.composeapp.generated.resources.dibujo007
import connexus_serverless.composeapp.generated.resources.dibujo008
import connexus_serverless.composeapp.generated.resources.dibujo009
import connexus_serverless.composeapp.generated.resources.dibujo010
import connexus_serverless.composeapp.generated.resources.dibujo011
import connexus_serverless.composeapp.generated.resources.dibujo012
import connexus_serverless.composeapp.generated.resources.dibujo013
import connexus_serverless.composeapp.generated.resources.dibujo014
import connexus_serverless.composeapp.generated.resources.dibujo015
import connexus_serverless.composeapp.generated.resources.dibujo016
import connexus_serverless.composeapp.generated.resources.dibujo017
import connexus_serverless.composeapp.generated.resources.dibujo018
import connexus_serverless.composeapp.generated.resources.dibujo019
import connexus_serverless.composeapp.generated.resources.dibujo020
import connexus_serverless.composeapp.generated.resources.dibujo021
import connexus_serverless.composeapp.generated.resources.dibujo022
import connexus_serverless.composeapp.generated.resources.dibujo023
import connexus_serverless.composeapp.generated.resources.dibujo024
import connexus_serverless.composeapp.generated.resources.dibujo025
import connexus_serverless.composeapp.generated.resources.dibujo026
import connexus_serverless.composeapp.generated.resources.dibujo027
import connexus_serverless.composeapp.generated.resources.dibujo028
import connexus_serverless.composeapp.generated.resources.dibujo029
import connexus_serverless.composeapp.generated.resources.dibujo030
import connexus_serverless.composeapp.generated.resources.dibujo031
import connexus_serverless.composeapp.generated.resources.dibujo032
import connexus_serverless.composeapp.generated.resources.ic_chats
import connexus_serverless.composeapp.generated.resources.ic_email
import connexus_serverless.composeapp.generated.resources.ic_foros
import connexus_serverless.composeapp.generated.resources.ic_person
import connexus_serverless.composeapp.generated.resources.persona001
import connexus_serverless.composeapp.generated.resources.persona002
import connexus_serverless.composeapp.generated.resources.persona003
import connexus_serverless.composeapp.generated.resources.persona004
import connexus_serverless.composeapp.generated.resources.persona005
import connexus_serverless.composeapp.generated.resources.persona006
import connexus_serverless.composeapp.generated.resources.persona007
import connexus_serverless.composeapp.generated.resources.persona008
import connexus_serverless.composeapp.generated.resources.persona009
import connexus_serverless.composeapp.generated.resources.persona010
import connexus_serverless.composeapp.generated.resources.persona011
import connexus_serverless.composeapp.generated.resources.persona012
import connexus_serverless.composeapp.generated.resources.persona013
import connexus_serverless.composeapp.generated.resources.persona014
import connexus_serverless.composeapp.generated.resources.persona015
import connexus_serverless.composeapp.generated.resources.persona016
import connexus_serverless.composeapp.generated.resources.persona017
import connexus_serverless.composeapp.generated.resources.persona018
import connexus_serverless.composeapp.generated.resources.persona019
import connexus_serverless.composeapp.generated.resources.persona020
import connexus_serverless.composeapp.generated.resources.persona021
import connexus_serverless.composeapp.generated.resources.persona022
import connexus_serverless.composeapp.generated.resources.persona023
import connexus_serverless.composeapp.generated.resources.persona024
import connexus_serverless.composeapp.generated.resources.persona025
import connexus_serverless.composeapp.generated.resources.persona026
import connexus_serverless.composeapp.generated.resources.persona027
import connexus_serverless.composeapp.generated.resources.persona028
import connexus_serverless.composeapp.generated.resources.persona029
import connexus_serverless.composeapp.generated.resources.persona030
import connexus_serverless.composeapp.generated.resources.unblock
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.datos.UsuarioPrincipal
import org.connexuss.project.firebase.pruebas.AppFirebase
import org.connexuss.project.firebase.pruebas.FirestoreConversacionesRepositorio
import org.connexuss.project.firebase.pruebas.FirestoreConversacionesUsuariosRepositorio
import org.connexuss.project.firebase.pruebas.FirestoreHilosRepositorio
import org.connexuss.project.firebase.pruebas.FirestoreMensajesRepositorio
import org.connexuss.project.firebase.pruebas.FirestorePostsRepositorio
import org.connexuss.project.firebase.pruebas.FirestoreTemasRepositorio
import org.connexuss.project.firebase.pruebas.FirestoreUsuariosNuestros
import org.connexuss.project.firebase.pruebas.FirestoreUsuariosRepositorio
import org.connexuss.project.firebase.pruebas.MuestraObjetosPruebasFriebase
import org.connexuss.project.firebase.pruebas.PantallaConversacion
import org.connexuss.project.firebase.pruebas.PantallaConversacionUsuario
import org.connexuss.project.firebase.pruebas.PantallaHilo
import org.connexuss.project.firebase.pruebas.PantallaMensaje
import org.connexuss.project.firebase.pruebas.PantallaPost
import org.connexuss.project.firebase.pruebas.PantallaTema
import org.connexuss.project.firebase.pruebas.PantallaUsuario
import org.connexuss.project.firebase.pruebas.PantallaUsuarioNuestro
import org.connexuss.project.interfaces.ForoScreen
import org.connexuss.project.interfaces.MuestraUsuariosGrupo
import org.connexuss.project.interfaces.PantallaAjustesAyuda
import org.connexuss.project.interfaces.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.PantallaCambiarFuente
import org.connexuss.project.interfaces.PantallaCambiarTema
import org.connexuss.project.interfaces.PantallaEmailEnElSistema
import org.connexuss.project.interfaces.PantallaEmailNoEnElSistema
import org.connexuss.project.interfaces.PantallaForoLocal
import org.connexuss.project.interfaces.PantallaHiloLocal
import org.connexuss.project.interfaces.PantallaIdiomas
import org.connexuss.project.interfaces.PantallaLogin
import org.connexuss.project.interfaces.PantallaRegistro
import org.connexuss.project.interfaces.PantallaRestablecer
import org.connexuss.project.interfaces.PantallaTemaLocal
import org.connexuss.project.interfaces.SplashScreen
import org.connexuss.project.interfaces.TemaConfig
import org.connexuss.project.interfaces.mostrarChat
import org.connexuss.project.interfaces.mostrarChatGrupo
import org.connexuss.project.interfaces.mostrarPerfil
import org.connexuss.project.interfaces.mostrarPerfilUsuario
import org.connexuss.project.interfaces.muestraAjustes
import org.connexuss.project.interfaces.muestraChats
import org.connexuss.project.interfaces.muestraContactos
import org.connexuss.project.interfaces.muestraHomePage
import org.connexuss.project.interfaces.muestraRestablecimientoContasenna
import org.connexuss.project.interfaces.muestraUsuarios
import org.connexuss.project.misc.Imagen
import org.connexuss.project.usuario.Usuario

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit,
    listaUsuariosGrupo: List<Usuario>
) {
    val navController = rememberNavController()

    val repositorioUsuarios = remember { FirestoreUsuariosRepositorio() }
    val repositorioUsuariosNuestros = remember { FirestoreUsuariosNuestros() }
    val repositorioMensajes = remember { FirestoreMensajesRepositorio() }
    val repositorioConversaciones = remember { FirestoreConversacionesRepositorio() }
    val repositorioConversacionesUsuarios = remember { FirestoreConversacionesUsuariosRepositorio() }
    val repositorioPosts = remember { FirestorePostsRepositorio() }
    val repositorioHilos = remember { FirestoreHilosRepositorio() }
    val repositorioTemas = remember { FirestoreTemasRepositorio() }

    var imagenesApp = mutableListOf<Imagen>()
    imagenesApp.add(Imagen("logo", Res.drawable.connexus))
    imagenesApp.add(Imagen("avatar", Res.drawable.avatar))
    imagenesApp.add(Imagen("unblock", Res.drawable.unblock))
    imagenesApp.add(Imagen("ic_chats", Res.drawable.ic_chats))
    imagenesApp.add(Imagen("ic_email", Res.drawable.ic_email))
    imagenesApp.add(Imagen("ic_foros", Res.drawable.ic_foros))
    imagenesApp.add(Imagen("ic_person", Res.drawable.ic_person))
    imagenesApp.add(Imagen("visibilidadOn", Res.drawable.visibilidadOn))
    imagenesApp.add(Imagen("visibilidadOff", Res.drawable.visibilidadOff))

    val imagenesPerfilPersona = mutableListOf<Imagen>()
    imagenesPerfilPersona.add(Imagen("persona001", Res.drawable.persona001))
    imagenesPerfilPersona.add(Imagen("persona002", Res.drawable.persona002))
    imagenesPerfilPersona.add(Imagen("persona003", Res.drawable.persona003))
    imagenesPerfilPersona.add(Imagen("persona004", Res.drawable.persona004))
    imagenesPerfilPersona.add(Imagen("persona005", Res.drawable.persona005))
    imagenesPerfilPersona.add(Imagen("persona006", Res.drawable.persona006))
    imagenesPerfilPersona.add(Imagen("persona007", Res.drawable.persona007))
    imagenesPerfilPersona.add(Imagen("persona008", Res.drawable.persona008))
    imagenesPerfilPersona.add(Imagen("persona009", Res.drawable.persona009))
    imagenesPerfilPersona.add(Imagen("persona010", Res.drawable.persona010))
    imagenesPerfilPersona.add(Imagen("persona011", Res.drawable.persona011))
    imagenesPerfilPersona.add(Imagen("persona012", Res.drawable.persona012))
    imagenesPerfilPersona.add(Imagen("persona013", Res.drawable.persona013))
    imagenesPerfilPersona.add(Imagen("persona014", Res.drawable.persona014))
    imagenesPerfilPersona.add(Imagen("persona015", Res.drawable.persona015))
    imagenesPerfilPersona.add(Imagen("persona016", Res.drawable.persona016))
    imagenesPerfilPersona.add(Imagen("persona017", Res.drawable.persona017))
    imagenesPerfilPersona.add(Imagen("persona018", Res.drawable.persona018))
    imagenesPerfilPersona.add(Imagen("persona019", Res.drawable.persona019))
    imagenesPerfilPersona.add(Imagen("persona020", Res.drawable.persona020))
    imagenesPerfilPersona.add(Imagen("persona021", Res.drawable.persona021))
    imagenesPerfilPersona.add(Imagen("persona022", Res.drawable.persona022))
    imagenesPerfilPersona.add(Imagen("persona023", Res.drawable.persona023))
    imagenesPerfilPersona.add(Imagen("persona024", Res.drawable.persona024))
    imagenesPerfilPersona.add(Imagen("persona025", Res.drawable.persona025))
    imagenesPerfilPersona.add(Imagen("persona026", Res.drawable.persona026))
    imagenesPerfilPersona.add(Imagen("persona027", Res.drawable.persona027))
    imagenesPerfilPersona.add(Imagen("persona028", Res.drawable.persona028))
    imagenesPerfilPersona.add(Imagen("persona029", Res.drawable.persona029))
    imagenesPerfilPersona.add(Imagen("persona030", Res.drawable.persona030))

    val imagenesPerfilAbstrasto = mutableListOf<Imagen>()
    imagenesPerfilAbstrasto.add(Imagen("abstracto001", Res.drawable.abstracto001))
    imagenesPerfilAbstrasto.add(Imagen("abstracto002", Res.drawable.abstracto002))
    imagenesPerfilAbstrasto.add(Imagen("abstracto003", Res.drawable.abstracto003))
    imagenesPerfilAbstrasto.add(Imagen("abstracto004", Res.drawable.abstracto004))
    imagenesPerfilAbstrasto.add(Imagen("abstracto005", Res.drawable.abstracto005))
    imagenesPerfilAbstrasto.add(Imagen("abstracto006", Res.drawable.abstracto006))
    imagenesPerfilAbstrasto.add(Imagen("abstracto007", Res.drawable.abstracto007))
    imagenesPerfilAbstrasto.add(Imagen("abstracto008", Res.drawable.abstracto008))
    imagenesPerfilAbstrasto.add(Imagen("abstracto009", Res.drawable.abstracto009))
    imagenesPerfilAbstrasto.add(Imagen("abstracto010", Res.drawable.abstracto010))
    imagenesPerfilAbstrasto.add(Imagen("abstracto011", Res.drawable.abstracto011))
    imagenesPerfilAbstrasto.add(Imagen("abstracto012", Res.drawable.abstracto012))
    imagenesPerfilAbstrasto.add(Imagen("abstracto013", Res.drawable.abstracto013))
    imagenesPerfilAbstrasto.add(Imagen("abstracto014", Res.drawable.abstracto014))
    imagenesPerfilAbstrasto.add(Imagen("abstracto015", Res.drawable.abstracto015))
    imagenesPerfilAbstrasto.add(Imagen("abstracto016", Res.drawable.abstracto016))
    imagenesPerfilAbstrasto.add(Imagen("abstracto017", Res.drawable.abstracto017))
    imagenesPerfilAbstrasto.add(Imagen("abstracto018", Res.drawable.abstracto018))
    imagenesPerfilAbstrasto.add(Imagen("abstracto019", Res.drawable.abstracto019))
    imagenesPerfilAbstrasto.add(Imagen("abstracto020", Res.drawable.abstracto020))
    imagenesPerfilAbstrasto.add(Imagen("abstracto021", Res.drawable.abstracto021))
    imagenesPerfilAbstrasto.add(Imagen("abstracto022", Res.drawable.abstracto022))
    imagenesPerfilAbstrasto.add(Imagen("abstracto023", Res.drawable.abstracto023))
    imagenesPerfilAbstrasto.add(Imagen("abstracto024", Res.drawable.abstracto024))
    imagenesPerfilAbstrasto.add(Imagen("abstracto025", Res.drawable.abstracto025))
    imagenesPerfilAbstrasto.add(Imagen("abstracto026", Res.drawable.abstracto026))
    imagenesPerfilAbstrasto.add(Imagen("abstracto027", Res.drawable.abstracto027))
    imagenesPerfilAbstrasto.add(Imagen("abstracto028", Res.drawable.abstracto028))
    imagenesPerfilAbstrasto.add(Imagen("abstracto029", Res.drawable.abstracto029))
    imagenesPerfilAbstrasto.add(Imagen("abstracto030", Res.drawable.abstracto030))
    imagenesPerfilAbstrasto.add(Imagen("abstracto031", Res.drawable.abstracto031))
    imagenesPerfilAbstrasto.add(Imagen("abstracto032", Res.drawable.abstracto032))
    imagenesPerfilAbstrasto.add(Imagen("abstracto033", Res.drawable.abstracto033))
    imagenesPerfilAbstrasto.add(Imagen("abstracto034", Res.drawable.abstracto034))
    imagenesPerfilAbstrasto.add(Imagen("abstracto035", Res.drawable.abstracto035))
    imagenesPerfilAbstrasto.add(Imagen("abstracto036", Res.drawable.abstracto036))
    imagenesPerfilAbstrasto.add(Imagen("abstracto037", Res.drawable.abstracto037))
    imagenesPerfilAbstrasto.add(Imagen("abstracto038", Res.drawable.abstracto038))
    imagenesPerfilAbstrasto.add(Imagen("abstracto039", Res.drawable.abstracto039))
    imagenesPerfilAbstrasto.add(Imagen("abstracto040", Res.drawable.abstracto040))
    imagenesPerfilAbstrasto.add(Imagen("abstracto041", Res.drawable.abstracto041))
    imagenesPerfilAbstrasto.add(Imagen("abstracto042", Res.drawable.abstracto042))
    imagenesPerfilAbstrasto.add(Imagen("abstracto043", Res.drawable.abstracto043))
    imagenesPerfilAbstrasto.add(Imagen("abstracto044", Res.drawable.abstracto044))
    imagenesPerfilAbstrasto.add(Imagen("abstracto045", Res.drawable.abstracto045))
    imagenesPerfilAbstrasto.add(Imagen("abstracto046", Res.drawable.abstracto046))
    imagenesPerfilAbstrasto.add(Imagen("abstracto047", Res.drawable.abstracto047))
    imagenesPerfilAbstrasto.add(Imagen("abstracto048", Res.drawable.abstracto048))
    imagenesPerfilAbstrasto.add(Imagen("abstracto049", Res.drawable.abstracto049))
    imagenesPerfilAbstrasto.add(Imagen("abstracto050", Res.drawable.abstracto050))
    imagenesPerfilAbstrasto.add(Imagen("abstracto051", Res.drawable.abstracto051))

    val imagenesPerfilDibujo = mutableListOf<Imagen>()
    imagenesPerfilDibujo.add(Imagen("dibujo001", Res.drawable.dibujo001))
    imagenesPerfilDibujo.add(Imagen("dibujo002", Res.drawable.dibujo002))
    imagenesPerfilDibujo.add(Imagen("dibujo003", Res.drawable.dibujo003))
    imagenesPerfilDibujo.add(Imagen("dibujo004", Res.drawable.dibujo004))
    imagenesPerfilDibujo.add(Imagen("dibujo005", Res.drawable.dibujo005))
    imagenesPerfilDibujo.add(Imagen("dibujo006", Res.drawable.dibujo006))
    imagenesPerfilDibujo.add(Imagen("dibujo007", Res.drawable.dibujo007))
    imagenesPerfilDibujo.add(Imagen("dibujo008", Res.drawable.dibujo008))
    imagenesPerfilDibujo.add(Imagen("dibujo009", Res.drawable.dibujo009))
    imagenesPerfilDibujo.add(Imagen("dibujo010", Res.drawable.dibujo010))
    imagenesPerfilDibujo.add(Imagen("dibujo011", Res.drawable.dibujo011))
    imagenesPerfilDibujo.add(Imagen("dibujo012", Res.drawable.dibujo012))
    imagenesPerfilDibujo.add(Imagen("dibujo013", Res.drawable.dibujo013))
    imagenesPerfilDibujo.add(Imagen("dibujo014", Res.drawable.dibujo014))
    imagenesPerfilDibujo.add(Imagen("dibujo015", Res.drawable.dibujo015))
    imagenesPerfilDibujo.add(Imagen("dibujo016", Res.drawable.dibujo016))
    imagenesPerfilDibujo.add(Imagen("dibujo017", Res.drawable.dibujo017))
    imagenesPerfilDibujo.add(Imagen("dibujo018", Res.drawable.dibujo018))
    imagenesPerfilDibujo.add(Imagen("dibujo019", Res.drawable.dibujo019))
    imagenesPerfilDibujo.add(Imagen("dibujo020", Res.drawable.dibujo020))
    imagenesPerfilDibujo.add(Imagen("dibujo021", Res.drawable.dibujo021))
    imagenesPerfilDibujo.add(Imagen("dibujo022", Res.drawable.dibujo022))
    imagenesPerfilDibujo.add(Imagen("dibujo023", Res.drawable.dibujo023))
    imagenesPerfilDibujo.add(Imagen("dibujo024", Res.drawable.dibujo024))
    imagenesPerfilDibujo.add(Imagen("dibujo025", Res.drawable.dibujo025))
    imagenesPerfilDibujo.add(Imagen("dibujo026", Res.drawable.dibujo026))
    imagenesPerfilDibujo.add(Imagen("dibujo027", Res.drawable.dibujo027))
    imagenesPerfilDibujo.add(Imagen("dibujo028", Res.drawable.dibujo028))
    imagenesPerfilDibujo.add(Imagen("dibujo029", Res.drawable.dibujo029))
    imagenesPerfilDibujo.add(Imagen("dibujo030", Res.drawable.dibujo030))
    imagenesPerfilDibujo.add(Imagen("dibujo031", Res.drawable.dibujo031))
    imagenesPerfilDibujo.add(Imagen("dibujo032", Res.drawable.dibujo032))


    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            muestraHomePage(navController)
        }
        composable("login") {
            PantallaLogin(navController)
        }
        composable("registro") {
            PantallaRegistro(navController)
        }
        composable("ajustes") {
            muestraAjustes(navController)
        }
        composable("contactos") {
            muestraChats(navController)
        }
        composable("nuevo") {
            muestraContactos(navController)
        }
        composable("restablecer") {
            PantallaRestablecer(navController)
        }
        composable("usuarios") {
            muestraUsuarios(navController)
        }
        composable("emailEnSistema") {
            PantallaEmailEnElSistema(navController)
        }
        composable("emailNoEnSistema") {
            PantallaEmailNoEnElSistema(navController)
        }
        composable("restableceContrasenna") {
            muestraRestablecimientoContasenna(navController)
        }
        composable("ajustesControlCuentas") {
            PantallaAjustesControlCuentas(navController)
        }
        composable("ajustesAyuda") {
            PantallaAjustesAyuda(navController)
        }
        composable("cambiarTema") {
            PantallaCambiarTema(
                navController = navController,
                temaConfig = temaConfig,
                onToggleTheme = onToggleTheme,
                onColorChange = onColorChange // Pasa la función directamente
            )
        }
        composable("mostrarPerfilPrincipal") {
            mostrarPerfil(navController, UsuarioPrincipal)
        }
        composable("mostrarChat/{chatId}") {
            backStackEntry ->
            // Obtener chatId de los argumentos
            val chatId = backStackEntry.arguments?.getString("chatId")
                mostrarChat(navController, chatId)
        }
        composable("mostrarChatGrupo/{chatId}") {
            backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChatGrupo(navController, chatId, imagenesPerfilPersona)
        }
//        composable("foro") {
//            muestraForo(navController)
//        }
//        composable("temaForo") {
//            muestraTemaForo(navController)
//        }
        composable("idiomas") {
            PantallaIdiomas(navController)
        }
        composable("cambiaFuente") {
            PantallaCambiarFuente(navController)
        }
        composable("mostrarPerfilUsuario/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            mostrarPerfilUsuario(navController, userId, imagenesPerfilPersona)
        }
        composable("appFirebase") {
            AppFirebase(navController)
        }
        composable("pruebasObjetosFIrebase") {
            MuestraObjetosPruebasFriebase(navController)
        }
        /*
        composable("pruebas/{objeto}") { backStackEntry ->
            val objeto = backStackEntry.arguments?.getString("objeto")
            Text("Objeto: $objeto")
        }
        */
        composable("UsuarioPrueba") {
            PantallaUsuario(repositorioUsuarios, navController)
        }
        composable("Usuario") {
            PantallaUsuarioNuestro(repositorioUsuariosNuestros, navController)
        }
        composable("Mensaje") {
            PantallaMensaje(repositorioMensajes, navController)
        }
        composable("Conversacion") {
            PantallaConversacion(repositorioConversaciones, navController)
        }
        composable("ConversacionUsuario") {
            PantallaConversacionUsuario(repositorioConversacionesUsuarios, navController)
        }
        composable("Post") {
            PantallaPost(repositorioPosts, navController)
        }
        composable("Hilo") {
            PantallaHilo(repositorioHilos, navController)
        }
        composable("Tema") {
            PantallaTema(repositorioTemas, navController)
        }
        composable("mostrarParticipantesGrupo/{chatId}" ) {
            backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChatGrupo(navController, chatId, imagenesPerfilPersona)
        }
        composable("usuariosGrupo") {
            MuestraUsuariosGrupo(usuarios = listaUsuariosGrupo, navController = navController)
        }
//        composable("foroLocal") {
//            PantallaForoLocal(navController)
//        }

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

        // Pruebas interfaces foro ------
        composable("foroLocal") {
            ForoScreen(navController, temasHilosPosts)
        }


        // Fíjate en la barra y en las llaves:
        composable("tema/{tema}") { backStackEntry ->
            // Recuperas el valor de "tema"
            val temaParam = backStackEntry.arguments?.getString("tema") ?: ""
            // Lo pasas a tu pantalla
            PantallaTemaLocal(navController, temaParam)
        }
        composable("hilo/{mensaje}") { backStackEntry ->
            val mensajeParam = backStackEntry.arguments?.getString("mensaje") ?: ""
            PantallaHiloLocal(navController, mensajeParam)
        }
    }
}