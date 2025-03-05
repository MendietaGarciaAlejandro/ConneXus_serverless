package org.connexuss.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.connexuss.project.navegacion.Navegacion
import org.connexuss.project.navegacion.Routes
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {

    // Intento de navegacion entre pantallas
    /*
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Routes.HomeGraph
        ) {
            navigation<Routes.HomeGraph>(
                startDestination = Routes.Home
            ) {
                composable<Routes.Home> {  }
            }
            )
        }
    }
     */

    // Funcion para navegar entre pantallas (no funciona)
    Navegacion()


    // Descomenta y navega entre pantallas para ver la interfaz
    //muestraUsuarios()
    //pantallaRegistro()
    //pantallaLogin()
    //restableceContrasenna()
    //muestraHomePage()
    //muestraChatRoom()
    //muestraContactos()
    //muestraAjustes()
    //muestraNuevoChat()
    //muestraChats()
    //muestraForo()
    //muestraTemaForo()
    //prueba

}