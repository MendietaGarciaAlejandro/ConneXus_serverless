package org.connexuss.project.navegacion

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.connexuss.project.ListaCompletaColores
import org.connexuss.project.TemaConfig

import org.connexuss.project.interfaces.sistema.SplashScreen
import org.connexuss.project.interfaces.sistema.muestraAjustes
import org.connexuss.project.interfaces.sistema.muestraChats
import org.connexuss.project.interfaces.sistema.muestraHomePage
import org.connexuss.project.interfaces.sistema.muestraUsuarios

import org.connexuss.project.interfaces.ajustes.PantallaAjustesAyuda
import org.connexuss.project.interfaces.ajustes.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.ajustes.PantallaCambiarTema
import org.connexuss.project.interfaces.foro.muestraForo
import org.connexuss.project.interfaces.foro.muestraTemaForo
import org.connexuss.project.interfaces.idiomas.PantallaIdiomas
import org.connexuss.project.interfaces.sistema.*

@Composable
fun Navegacion(
    temaConfig: TemaConfig,
    onToggleTheme: () -> Unit,
    onColorChange: (Colors) -> Unit
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
            muestraContactos(navController, GeneraUsuarios())
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
        composable("ajustesControlCuentas") {
            PantallaAjustesControlCuentas(navController)
        }
        composable("ajustesAyuda") {
            PantallaAjustesAyuda(navController)
        }
        composable("cambiarTema") {
            PantallaCambiarTema(navController = navController, temaConfig = temaConfig, onToggleTheme = onToggleTheme, onColorChange = onColorChange)
        }
        composable("mostrarPerfil/{userId}") {
            backStackEntry ->
            // Obtener userId de los argumentos
            val userId = backStackEntry.arguments?.getString("userId")
            mostrarPerfil(navController, userId)
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
    }
}