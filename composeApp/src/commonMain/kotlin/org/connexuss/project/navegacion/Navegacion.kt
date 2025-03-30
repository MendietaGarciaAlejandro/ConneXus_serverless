package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.imagenesPerfilPersona
import org.connexuss.project.misc.temasHilosPosts
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
import org.connexuss.project.interfaces.HiloScreen
import org.connexuss.project.interfaces.MuestraUsuariosGrupo
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
import org.connexuss.project.interfaces.TemaScreen
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
        composable("foroLocal") {
            ForoScreen(navController)
        }
        composable("tema/{temaId}") { backStackEntry ->
            val temaId = backStackEntry.arguments?.getString("temaId")
            // Buscar el Tema en tu lista global o pasarlo como argumento
            val temaEncontrado = temasHilosPosts.find { it.idTema == temaId } ?: return@composable
            TemaScreen(navController, temaEncontrado.idTema)
        }
        composable("hilo/{hiloId}") { backStackEntry ->
            val hiloId = backStackEntry.arguments?.getString("hiloId")
            // Buscar el Hilo en tu estado global o pasarlo como argumento
            val hiloEncontrado = temasHilosPosts.flatMap { it.hilos }.find { it.idHilo == hiloId } ?: return@composable
            HiloScreen(navController, hiloEncontrado.idHilo)
        }
    }
}