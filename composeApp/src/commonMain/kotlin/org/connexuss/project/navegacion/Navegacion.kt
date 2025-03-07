package org.connexuss.project.navegacion


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import org.connexuss.project.interfaces.sistema.SplashScreen
import org.connexuss.project.interfaces.sistema.muestraAjustes
import org.connexuss.project.interfaces.sistema.muestraChats
import org.connexuss.project.interfaces.sistema.muestraHomePage
import org.connexuss.project.interfaces.sistema.muestraUsuarios

import org.connexuss.project.interfaces.ajustes.PantallaAjustesAyuda
import org.connexuss.project.interfaces.ajustes.PantallaAjustesControlCuentas
import org.connexuss.project.interfaces.sistema.*


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
            //pantallaLogin(navController)
            PantallaLogin(navController)
        }
        composable("registro") {
            //pantallaRegistro(navController)
            PantallaRegistro(navController)
        }
        composable("ajustes") {
            muestraAjustes(navController)
        }
        //para mostrar los  datos predefinidos contactos
        composable("contactos") {
            muestraChats(navController)
        }
        composable("nuevo") {
            muestraContactos(navController, GeneraUsuarios())
        }
        composable("restablecer") {
            //restableceContrasenna(navController)
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
    }
}