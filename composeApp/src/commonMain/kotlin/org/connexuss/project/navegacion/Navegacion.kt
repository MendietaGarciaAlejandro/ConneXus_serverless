package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.ic_chats
import connexus_serverless.composeapp.generated.resources.ic_email
import connexus_serverless.composeapp.generated.resources.ic_foros
import connexus_serverless.composeapp.generated.resources.ic_person
import connexus_serverless.composeapp.generated.resources.unblock
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
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
import org.connexuss.project.interfaces.PantallaAjustesAyuda
import org.connexuss.project.interfaces.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.PantallaCambiarFuente
import org.connexuss.project.interfaces.PantallaCambiarTema
import org.connexuss.project.interfaces.PantallaEmailEnElSistema
import org.connexuss.project.interfaces.PantallaEmailNoEnElSistema
import org.connexuss.project.interfaces.PantallaIdiomas
import org.connexuss.project.interfaces.PantallaLogin
import org.connexuss.project.interfaces.PantallaRegistro
import org.connexuss.project.interfaces.PantallaRestablecer
import org.connexuss.project.interfaces.SplashScreen
import org.connexuss.project.interfaces.TemaConfig
import org.connexuss.project.interfaces.mostrarChat
import org.connexuss.project.interfaces.mostrarChatGrupo
import org.connexuss.project.interfaces.mostrarPerfil
import org.connexuss.project.interfaces.mostrarPerfilUsuario
import org.connexuss.project.interfaces.muestraAjustes
import org.connexuss.project.interfaces.muestraChats
import org.connexuss.project.interfaces.muestraContactos
import org.connexuss.project.interfaces.muestraForo
import org.connexuss.project.interfaces.muestraHomePage
import org.connexuss.project.interfaces.muestraRestablecimientoContasenna
import org.connexuss.project.interfaces.muestraTemaForo
import org.connexuss.project.interfaces.muestraUsuarios
import org.connexuss.project.misc.Imagen

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit
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
            mostrarChatGrupo(navController, chatId, imagenesApp)
        }
        composable("foro") {
            muestraForo(navController)
        }
        composable("temaForo") {
            muestraTemaForo(navController)
        }
        composable("idiomas") {
            PantallaIdiomas(navController)
        }
        composable("cambiaFuente") {
            PantallaCambiarFuente(navController)
        }
        composable("mostrarPerfilUsuario/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            mostrarPerfilUsuario(navController, userId, imagenesApp)
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
    }
}