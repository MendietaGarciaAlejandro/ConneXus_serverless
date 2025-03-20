package org.connexuss.project.firebase.pruebas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.usuario.Usuario

@Composable
fun ListaUsuarios(usuarioPruebas: List<UsuarioPrueba>, modifier: Modifier = Modifier, userClicked: (UsuarioPrueba) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(usuarioPruebas, key = { it.idUsuarioPrueba }) { user ->
            ItemUsuario(usuarioPrueba = user) {
                userClicked(user)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaUsuariosNuestros(usuarios: List<Usuario>, modifier: Modifier = Modifier, userClicked: (Usuario) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(usuarios, key = { it.getIdUnico() }) { user ->
            ItemUsuarioNuestro(usuario = user) {
                userClicked(user)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaMensajes(mensajes: List<Mensaje>, modifier: Modifier = Modifier, mensajeClicked: (Mensaje) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(mensajes, key = { it.id }) { mensaje ->
            ItemMensaje(mensaje = mensaje) {
                mensajeClicked(mensaje)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaConversaciones(conversaciones: List<Conversacion>, modifier: Modifier = Modifier, conversacionClicked: (Conversacion) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(conversaciones, key = { it.id }) { conversacion ->
            ItemConversacion(conversacion = conversacion) {
                conversacionClicked(conversacion)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaConversacionesUsuarios(conversacionesUsuarios: List<ConversacionesUsuario>, modifier: Modifier = Modifier, conversacionUsuarioClicked: (ConversacionesUsuario) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(conversacionesUsuarios, key = { it.id }) { conversacionUsuario ->
            ItemConversacionesUsuario(conversacionesUsuario = conversacionUsuario) {
                conversacionUsuarioClicked(conversacionUsuario)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaPosts(posts: List<Post>, modifier: Modifier = Modifier, postClicked: (Post) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(posts, key = { it.idPost }) { post ->
            ItemPost(post = post) {
                postClicked(post)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaHilos(hilos: List<Hilo>, modifier: Modifier = Modifier, hiloClicked: (Hilo) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(hilos, key = { it.idHilo }) { hilo ->
            ItemHilo(hilo = hilo) {
                hiloClicked(hilo)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListaTemas(temas: List<Tema>, modifier: Modifier = Modifier, temaClicked: (Tema) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(temas, key = { it.idTema }) { tema ->
            ItemTema(tema = tema) {
                temaClicked(tema)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}