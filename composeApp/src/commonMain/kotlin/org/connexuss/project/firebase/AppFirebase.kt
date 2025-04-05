package org.connexuss.project.firebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun AppFirebase(navHostController: NavHostController) {

    // Repositorios
    val repositorioUsuarios = remember { FirestoreUsuariosRepositorio() }
    //val repositorioMensajes = remember { FirestoreMensajesRepositorio() }
    //val repositorioConversaciones = remember { FirestoreConversacionesRepositorio() }
    //val repositorioConversacionesUsuarios = remember { FirestoreConversacionesUsuariosRepositorio() }
    //val repositorioPosts = remember { FirestorePostsRepositorio() }
    //val repositorioHilos = remember { FirestoreHilosRepositorio() }
    //val repositorioTemas = remember { FirestoreTemasRepositorio() }

    // Pantallas
    PantallaUsuario(repositorioUsuarios, navHostController)
    //PantallaMensaje(repositorioMensajes, navHostController)
    //PantallaConversacion(repositorioConversaciones, navHostController)
    //PantallaConversacionUsuario(repositorioConversacionesUsuarios, navHostController)
    //PantallaPost(repositorioPosts, navHostController)
    //PantallaHilo(repositorioHilos, navHostController)
    //PantallaTema(repositorioTemas, navHostController)
}