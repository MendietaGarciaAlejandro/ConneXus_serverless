package org.connexuss.project.navegacion


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.ChatMessage
import org.connexuss.project.comunicacion.ChatRoom
import org.connexuss.project.comunicacion.ChatsUsers
import org.connexuss.project.interfaces.sistema.SplashScreen
import org.connexuss.project.interfaces.sistema.muestraAjustes
import org.connexuss.project.interfaces.sistema.muestraChats
import org.connexuss.project.interfaces.sistema.muestraContactos
import org.connexuss.project.interfaces.sistema.muestraHomePage
import org.connexuss.project.interfaces.sistema.muestraUsuarios
import org.connexuss.project.interfaces.sistema.pantallaLogin
import org.connexuss.project.interfaces.sistema.pantallaRegistro
import org.connexuss.project.interfaces.sistema.restableceContrasenna

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            muestraHomePage(navController)
        }
        composable("login") {
            pantallaLogin(navController)
        }
        composable("registro") {
            pantallaRegistro(navController)
        }
        composable("ajustes") {
            muestraAjustes(navController)
        }

        //para mostrar los  datos predefinidos contactos
        composable("contactos") {


            muestraChats(navController)
        }

        composable("restablecer") {
            restableceContrasenna(navController)
        }
        composable("usuarios") {
            muestraUsuarios(navController)
        }
    }
}
