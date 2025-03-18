package org.connexuss.project.firebase.pruebas

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.DefaultTopBar

@Composable
fun MuestraObjetosPruebasFriebase(navHostController: NavHostController) {

    /*
    // Repositorios
    val repositorioUsuarios = remember { FirestoreUsuariosRepositorio() }
    val repositorioMensajes = remember { FirestoreMensajesRepositorio() }
    val repositorioConversaciones = remember { FirestoreConversacionesRepositorio() }
    val repositorioConversacionesUsuarios = remember { FirestoreConversacionesUsuariosRepositorio() }
    val repositorioPosts = remember { FirestorePostsRepositorio() }
    val repositorioHilos = remember { FirestoreHilosRepositorio() }
    val repositorioTemas = remember { FirestoreTemasRepositorio() }

    // Pantallas
    PantallaUsuario(repositorioUsuarios, navHostController)
     */

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Objetos Pruebas Firebase",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        }
    ) {
        val tiposObjetos = listOf(
            "UsuarioPrueba",
            "Mensaje",
            "Conversacion",
            "ConversacionUsuario",
            "Post",
            "Hilo",
            "Tema"
        )

        tiposObjetos.forEach { objeto ->
            Button(
                onClick = {
                    navHostController.navigate("pruebas/$objeto")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = objeto, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}