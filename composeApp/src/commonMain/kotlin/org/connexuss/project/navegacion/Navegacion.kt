package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.encriptacion.PantallaPruebasEncriptacion
/*
import org.connexuss.project.firebase.AppFirebase
import org.connexuss.project.firebase.FirestoreConversacionesRepositorio
import org.connexuss.project.firebase.FirestoreConversacionesUsuariosRepositorio
import org.connexuss.project.firebase.FirestoreHilosRepositorio
import org.connexuss.project.firebase.FirestoreMensajesRepositorio
import org.connexuss.project.firebase.FirestorePostsRepositorio
import org.connexuss.project.firebase.FirestoreTemasRepositorio
import org.connexuss.project.firebase.FirestoreUsuariosNuestros
import org.connexuss.project.firebase.FirestoreUsuariosRepositorio
import org.connexuss.project.firebase.MuestraObjetosPruebasFriebase
import org.connexuss.project.firebase.PantallaConversacion
import org.connexuss.project.firebase.PantallaConversacionUsuario
import org.connexuss.project.firebase.PantallaHilo
import org.connexuss.project.firebase.PantallaMensaje
import org.connexuss.project.firebase.PantallaPost
import org.connexuss.project.firebase.PantallaTema
import org.connexuss.project.firebase.PantallaUsuario
import org.connexuss.project.firebase.PantallaUsuarioNuestro
 */
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
import org.connexuss.project.interfaces.PantallaZonaPruebas
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
import org.connexuss.project.misc.HilosRepository
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.imagenesPerfilPersona
import org.connexuss.project.supabase.*
import org.connexuss.project.usuario.Usuario

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit,
    listaUsuariosGrupo: List<Usuario>
) {
    val navController = rememberNavController()

    /*
    val repositorioUsuarios = remember { FirestoreUsuariosRepositorio() }
    val repositorioUsuariosNuestros = remember { FirestoreUsuariosNuestros() }
    val repositorioMensajes = remember { FirestoreMensajesRepositorio() }
    val repositorioConversaciones = remember { FirestoreConversacionesRepositorio() }
    val repositorioConversacionesUsuarios = remember { FirestoreConversacionesUsuariosRepositorio() }
    val repositorioPosts = remember { FirestorePostsRepositorio() }
    val repositorioHilos = remember { FirestoreHilosRepositorio() }
    val repositorioTemas = remember { FirestoreTemasRepositorio() }
     */

    val repoSupabase = remember { SupabaseRepositorioGenerico() }

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
        /*
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
        */
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
        composable(
            "tema/{temaId}",
            arguments = listOf(navArgument("temaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val temaId = backStackEntry.arguments!!.getString("temaId")!!
            TemaScreen(navController, temaId)
        }
        composable(
            "hilo/{hiloId}",
            arguments = listOf(navArgument("hiloId") { type = NavType.StringType })
        ) {
            backStackEntry ->
            val hiloId = backStackEntry.arguments!!.getString("hiloId")!!
            HiloScreen(navController, hiloId)
        }
        composable("pruebasEncriptacion") {
            PantallaPruebasEncriptacion(navController)
        }
        composable("zonaPruebas") {
            PantallaZonaPruebas(navController)
        }
        composable("supabasePruebas") {
            SupabasePruebasInterfaz(navController)
        }
        composable("supabaseUsuariosCRUD") {
            SupabaseUsuariosCRUD(navController)
        }
        composable("supabasePruebasMensajes") {
            SupabaseMensajesCRUD(navController)
        }
        composable("supabasePruebasConversaciones") {
            SupabaseConversacionesCRUD(navController)
        }
        composable("supabasePruebasConversacionesUsuarioCRUD") {
            SupabaseConversacionesUsuarioCRUD(navController)
        }
        composable("supabasePruebasTemasCRUD") {
            SupabaseTemasCRUD(navController)
        }
        composable("supabasePruebasHilosCRUD") {
            SupabaseHilosCRUD(navController)
        }
        composable("supabasePruebasPostsCRUD") {
            SupabasePostsCRUD(navController)
        }
        composable("supabaseBloqueadosCRUD") {
            SupabaseBloqueadosCRUD(navController)
        }
        composable("supabaseContactosCRUD") {
            SupabaseContactosCRUD(navController)
        }
        composable("mostrarChat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChat(navController = navController, chatId = chatId)
        }
        composable("mostrarChatGrupo/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChatGrupo(navController = navController, chatId = chatId, imagenesPerfil = emptyList()) // puedes rellenar luego si quieres
        }
        composable("pruebasTextosRealtime") {
            PantallaTextosRealtime(navHostController = navController)
        }
    }
}