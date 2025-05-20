package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.Dispatchers
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
import org.connexuss.project.interfaces.usuario.MuestraUsuariosGrupo
import org.connexuss.project.interfaces.tema.PantallaCambiarFuente
import org.connexuss.project.interfaces.tema.PantallaCambiarTema
import org.connexuss.project.interfaces.comun.PantallaIdiomas
import org.connexuss.project.interfaces.ajustes.PantallaAjustesAyuda
import org.connexuss.project.interfaces.ajustes.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.autenticacion.PantallaEmailEnElSistema
import org.connexuss.project.interfaces.autenticacion.PantallaEmailNoEnElSistema
import org.connexuss.project.interfaces.autenticacion.PantallaLogin
import org.connexuss.project.interfaces.autenticacion.PantallaRegistro
import org.connexuss.project.interfaces.autenticacion.PantallaRestablecer
import org.connexuss.project.interfaces.autenticacion.PantallaRestablecimientoContrasenna
import org.connexuss.project.interfaces.autenticacion.PantallaVerificaCorreo
import org.connexuss.project.interfaces.autenticacion.SplashScreen
import org.connexuss.project.interfaces.chat.mostrarChat
import org.connexuss.project.interfaces.chat.mostrarChatGrupo
import org.connexuss.project.interfaces.chat.muestraChats
import org.connexuss.project.interfaces.foro.ForoScreen
import org.connexuss.project.interfaces.foro.HiloScreen
import org.connexuss.project.interfaces.foro.TemaScreen
import org.connexuss.project.interfaces.perfiles.mostrarPerfil
import org.connexuss.project.interfaces.perfiles.mostrarPerfilUsuario
import org.connexuss.project.interfaces.tema.TemaConfig
import org.connexuss.project.interfaces.pruebas.PantallaZonaPruebas
import org.connexuss.project.interfaces.sistema.muestraAjustes
import org.connexuss.project.interfaces.sistema.muestraHomePage
import org.connexuss.project.interfaces.usuario.muestraContactos
import org.connexuss.project.interfaces.usuario.muestraUsuarios
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.imagenesPerfilPersona
import org.connexuss.project.persistencia.FlowSettingsProvider
import org.connexuss.project.persistencia.PantallaPruebasPersistencia
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.settings
import org.connexuss.project.supabase.*
import org.connexuss.project.usuario.Usuario

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit,
    listaUsuariosGrupo: List<Usuario>,
    settingsState: SettingsState
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

    val estadoFlowSettings = remember { FlowSettingsProvider(settings, Dispatchers.Default) }
    // val estadoSettings = remember { SettingsState(  ) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController, settingsState)
        }
        composable("home") {
            muestraHomePage(navController)
        }
        composable("login") {
            PantallaLogin(navController, settingsState)
        }
        composable("registro") {
            PantallaRegistro(navController)
        }
        composable("ajustes") {
            muestraAjustes(navController, settingsState)
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
        composable("restablecerNueva") {
            PantallaRestablecimientoContrasenna(navController)
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
            PantallaRestablecimientoContrasenna(navController)
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
                settingsState = settingsState
            )
        }
        composable("mostrarPerfilPrincipal") {
            mostrarPerfil(navController, UsuarioPrincipal)
        }
        composable("mostrarChat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChat(navController, chatId)
        }

        // Chat grupal
        composable("mostrarChatGrupo/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            mostrarChatGrupo(navController, chatId, imagenesPerfil = emptyList())
        }
        composable("idiomas") {
            PantallaIdiomas(navController, settingsState)
        }
        composable("cambiaFuente") {
            PantallaCambiarFuente(navController, settingsState)
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
            HiloScreen(navController, hiloId, "foroLocal")
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
        composable("zonaReportes") {
            PantallaReportesRealtime(navHostController = navController)
        }
        composable("pruebasPersistencia") {
            PantallaPruebasPersistencia(estadoFlowSettings, navController)
        }
        composable(
            "registroVerificaCorreo/{email}/{nombre}/{password}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val nombre = backStackEntry.arguments?.getString("nombre")
            val password = backStackEntry.arguments?.getString("password")
            PantallaVerificaCorreo(navController, email, nombre, password)
        }


    }
}

