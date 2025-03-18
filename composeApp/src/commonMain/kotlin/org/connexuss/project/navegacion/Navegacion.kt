package org.connexuss.project.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.connexuss.project.datos.UsuarioPrincipal
import org.connexuss.project.firebase.pruebas.AppFirebase
import org.connexuss.project.interfaces.PantallaAjustesAyuda
import org.connexuss.project.interfaces.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.PantallaCambiarTema
import org.connexuss.project.interfaces.TemaConfig
import org.connexuss.project.interfaces.muestraForo
import org.connexuss.project.interfaces.muestraTemaForo
import org.connexuss.project.interfaces.PantallaCambiarFuente
import org.connexuss.project.interfaces.PantallaIdiomas
import org.connexuss.project.interfaces.PantallaEmailEnElSistema
import org.connexuss.project.interfaces.PantallaEmailNoEnElSistema
import org.connexuss.project.interfaces.PantallaLogin
import org.connexuss.project.interfaces.PantallaRegistro
import org.connexuss.project.interfaces.PantallaRestablecer
import org.connexuss.project.interfaces.SplashScreen
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

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (String) -> Unit
) {
    val navController = rememberNavController()
    var posicionColorPillado = 0
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
            mostrarChatGrupo(navController, chatId)
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
            mostrarPerfilUsuario(navController, userId)
        }
        composable("appFirebase") {
            AppFirebase(navController)
        }
    }
}