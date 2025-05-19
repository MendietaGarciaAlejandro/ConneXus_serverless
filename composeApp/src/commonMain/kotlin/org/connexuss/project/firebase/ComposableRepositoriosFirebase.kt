package org.connexuss.project.firebase
/*
@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.eq
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.usuario.Usuario
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun HojaContenidoAbajoUsuarios(
    usuarioPrueba: UsuarioPrueba? = null,
    onSave: (UsuarioPrueba) -> Unit,
    onDelete: (UsuarioPrueba?) -> Unit
) {
    var nombre by remember { mutableStateOf(usuarioPrueba?.nombre ?: "") }
    var titulo by remember { mutableStateOf(usuarioPrueba?.titulo ?: "") }
    var compannia by remember { mutableStateOf(usuarioPrueba?.compannia ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = titulo,
            onValueChange = { titulo = it },
            singleLine = true,
            label = { Text("Título") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = compannia,
            onValueChange = { compannia = it },
            singleLine = true,
            label = { Text("Compañía") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onSave(UsuarioPrueba(usuarioPrueba?.idUsuarioPrueba ?: "", nombre, titulo, compannia)) }) {
                Text(text = if (usuarioPrueba == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(usuarioPrueba) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoUsuariosNuestros(
    usuario: Usuario? = null,
    onSave: (Usuario) -> Unit,
    onDelete: (Usuario?) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario?.getNombreCompleto() ?: "") }
    var correo by remember { mutableStateOf(usuario?.getCorreo() ?: "") }
    var alias by remember { mutableStateOf(usuario?.getAlias() ?: "") }
    var aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivado() ?: "") }
    var idUnico by remember { mutableStateOf(usuario?.getIdUnico() ?: "") }
    var descripcion by remember { mutableStateOf(usuario?.getDescripcion() ?: "") }
    var contrasennia by remember { mutableStateOf(usuario?.getContrasennia() ?: "") }
    var usuariosBloqueados by remember { mutableStateOf(usuario?.getUsuariosBloqueados().toString()) }
    val chatUserPrueba = ConversacionesUsuario("", "", emptyList())
    val usuarioInterno = Usuario("", "", "", "", false, emptyList(), chatUserPrueba)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = correo,
            onValueChange = { correo = it },
            singleLine = true,
            label = { Text("Correo") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = alias,
            onValueChange = { alias = it },
            singleLine = true,
            label = { Text("Alias") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = aliasPrivado,
            onValueChange = { aliasPrivado = it },
            singleLine = true,
            label = { Text("Alias Privado") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = idUnico,
            onValueChange = { idUnico = it },
            singleLine = true,
            label = { Text("ID Único") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = descripcion,
            onValueChange = { descripcion = it },
            singleLine = true,
            label = { Text("Descripción") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = contrasennia,
            onValueChange = { contrasennia = it },
            singleLine = true,
            label = { Text("Contraseña") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = usuariosBloqueados,
            onValueChange = { usuariosBloqueados = it },
            singleLine = true,
            label = { Text("Usuarios Bloqueados") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                usuarioInterno.setNombreCompleto(nombre)
                usuarioInterno.setCorreo(correo)
                usuarioInterno.setAlias(alias)
                usuarioInterno.setAliasPrivado(aliasPrivado)
                usuarioInterno.setIdUnico(idUnico)
                usuarioInterno.setDescripcion(descripcion)
                usuarioInterno.setContrasennia(contrasennia)
                usuarioInterno.setUsuariosBloqueados(usuariosBloqueados.split(", "))
                onSave(usuarioInterno)
            }) {
                Text(text = if (usuario == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(usuario) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoMensajes(
    mensaje: Mensaje? = null,
    onSave: (Mensaje) -> Unit,
    onDelete: (Mensaje?) -> Unit
) {
    var contenido by remember { mutableStateOf(mensaje?.content ?: "") }
    var usuarioEnvia by remember { mutableStateOf(mensaje?.senderId ?: "") }
    var usuarioRecibe by remember { mutableStateOf(mensaje?.receiverId ?: "") }
    var fecha by remember { mutableStateOf(mensaje?.fechaMensaje.toString()) }
    val fechaHoy = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = contenido,
            onValueChange = { contenido = it },
            singleLine = true,
            label = { Text("Contenido") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = usuarioEnvia,
            onValueChange = { usuarioEnvia = it },
            singleLine = true,
            label = { Text("Usuario que envía") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = usuarioRecibe,
            onValueChange = { usuarioRecibe = it },
            singleLine = true,
            label = { Text("Usuario que recibe") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = fecha,
            onValueChange = { fecha = it },
            singleLine = true,
            label = { Text("Fecha") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onSave(Mensaje(mensaje?.id ?: "", usuarioEnvia, usuarioRecibe, contenido, /* LocalDateTime.parse(fecha) */ fechaHoy
            )) }) {
                Text(text = if (mensaje == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(mensaje) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoConversaciones(
    conversacion: Conversacion? = null,
    onSave: (Conversacion) -> Unit,
    onDelete: (Conversacion?) -> Unit
) {
    var nombre by remember { mutableStateOf(conversacion?.nombre ?: "") }
    var participantes by remember { mutableStateOf(conversacion?.participants?.joinToString(", ") ?: "") }
    var mensajes by remember { mutableStateOf(conversacion?.messages.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = participantes,
            onValueChange = { participantes = it },
            singleLine = true,
            label = { Text("Participantes") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = mensajes,
            onValueChange = { mensajes = it },
            singleLine = true,
            label = { Text("Mensajes") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onSave(Conversacion(conversacion?.id ?: "", participantes.split(", "), emptyList(), nombre)) }) {
                Text(text = if (conversacion == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(conversacion) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoConversacionesUsuarios(
    conversacionesUsuario: ConversacionesUsuario? = null,
    onSave: (ConversacionesUsuario) -> Unit,
    onDelete: (ConversacionesUsuario?) -> Unit
) {
    var id by remember { mutableStateOf(conversacionesUsuario?.id ?: "") }
    var idUser by remember { mutableStateOf(conversacionesUsuario?.idUser ?: "") }
    var conversaciones by remember { mutableStateOf(conversacionesUsuario?.conversaciones.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = { id = it },
            singleLine = true,
            label = { Text("ID") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = idUser,
            onValueChange = { idUser = it },
            singleLine = true,
            label = { Text("ID Usuario") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = conversaciones,
            onValueChange = { conversaciones = it },
            singleLine = true,
            label = { Text("Conversaciones") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onSave(ConversacionesUsuario(id, idUser, emptyList())) }) {
                Text(text = if (conversacionesUsuario == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(conversacionesUsuario) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoPosts(
    post: Post? = null,
    onSave: (Post) -> Unit,
    onDelete: (Post?) -> Unit
) {
    // Estado para cada campo (si el post existe, se usan sus valores; de lo contrario se inician vacíos)
    var id by remember { mutableStateOf(post?.idPost ?: "") }
    var contenido by remember { mutableStateOf(post?.content ?: "") }
    var idHilo by remember { mutableStateOf(post?.idHilo ?: "") }
    var idFirmante by remember { mutableStateOf(post?.idFirmante ?: "") }
    // Como fechaPost es de tipo LocalDateTime, se muestra su valor en String (se podría usar un DatePicker)
    var fecha by remember { mutableStateOf(post?.fechaPost.toString()) }

    // Se consulta el alias del usuario (se supone que existe el data class Usuario y la función getAlias())
    val repo = SupabaseRepositorioGenerico()
    val aliasPublicoUsuario = repo.getItem<Usuario>("usuario") {
        eq("idunico", idFirmante)
    }.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = { id = it },
            singleLine = true,
            label = { Text("ID Post") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = contenido,
            onValueChange = { contenido = it },
            singleLine = true,
            label = { Text("Contenido") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = idHilo,
            onValueChange = { idHilo = it },
            singleLine = true,
            label = { Text("ID Hilo") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = idFirmante,
            onValueChange = { idFirmante = it },
            singleLine = true,
            label = { Text("ID Firmante") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = fecha,
            onValueChange = { fecha = it },
            singleLine = true,
            label = { Text("Fecha Post") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onSave(Post(
                    // Si el ID no fue ingresado manualmente, se genera uno nuevo.
                    idPost = id.ifEmpty { generateId() },
                    content = contenido,
                    fechaPost = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    aliaspublico = aliasPublicoUsuario.value?.getAlias() ?: "",
                    idHilo = idHilo,
                    idFirmante = idFirmante
                ))
            }) {
                Text(text = if (post == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(post) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoHilos(
    hilo: Hilo? = null,
    onSave: (Hilo) -> Unit,
    onDelete: (Hilo?) -> Unit
) {
    var id by remember { mutableStateOf(hilo?.idHilo ?: "") }
    var nombre by remember { mutableStateOf(hilo?.nombre ?: "") }
    var idTema by remember { mutableStateOf(hilo?.idTema ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = { id = it },
            singleLine = true,
            label = { Text("ID Hilo") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre del Hilo") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = idTema,
            onValueChange = { idTema = it },
            singleLine = true,
            label = { Text("ID Tema") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onSave(Hilo(
                    idHilo = id.ifEmpty { generateId() },
                    nombre = nombre,
                    idTema = idTema
                ))
            }) {
                Text(text = if (hilo == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(hilo) }) {
                Text("Borrar")
            }
        }
    }
}

@Composable
fun HojaContenidoAbajoTemas(
    tema: Tema? = null,
    onSave: (Tema) -> Unit,
    onDelete: (Tema?) -> Unit
) {
    var id by remember { mutableStateOf(tema?.idTema ?: "") }
    var nombre by remember { mutableStateOf(tema?.nombre ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = { id = it },
            singleLine = true,
            label = { Text("ID Tema") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre del Tema") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onSave(Tema(
                    idTema = id.ifEmpty { generateId() },
                    nombre = nombre
                ))
            }) {
                Text(text = if (tema == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(tema) }) {
                Text("Borrar")
            }
        }
    }
}



@Composable
fun ItemUsuario(usuarioPrueba: UsuarioPrueba, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = usuarioPrueba.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = usuarioPrueba.titulo, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuarioPrueba.compannia, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ItemUsuarioNuestro(usuario: Usuario, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = usuario.getNombreCompleto(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = usuario.getCorreo(), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getAlias(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getAliasPrivado(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getIdUnico(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getDescripcion(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getContrasennia(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getUsuariosBloqueados().toString(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = usuario.getImagenPerfil().toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ItemMensaje(mensaje: Mensaje, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = mensaje.id, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = mensaje.senderId, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = mensaje.receiverId, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = mensaje.content, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = mensaje.fechaMensaje.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ItemConversacion(conversacion: Conversacion, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = conversacion.id, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = conversacion.participants.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = conversacion.messages.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = conversacion.nombre.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ItemConversacionesUsuario(conversacionesUsuario: ConversacionesUsuario, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = conversacionesUsuario.id, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = conversacionesUsuario.idUser, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = conversacionesUsuario.conversaciones.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ItemPost(post: Post, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "ID: ${post.idPost}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra el id del firmante (usuario)
            Text(text = "Firmante: ${post.idFirmante}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra el contenido del post
            Text(text = "Contenido: ${post.content}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra la fecha del post
            Text(text = "Fecha: ${post.fechaPost}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ItemHilo(hilo: Hilo, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "ID Hilo: ${hilo.idHilo}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra el nombre del hilo. Al ser opcional, se usa toString()
            Text(text = "Nombre: ${hilo.nombre ?: "Sin nombre"}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra el id del tema al que pertenece el hilo
            Text(text = "ID Tema: ${hilo.idTema}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ItemTema(tema: Tema, clicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "ID Tema: ${tema.idTema}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            // Se muestra el nombre del tema
            Text(text = "Nombre: ${tema.nombre}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


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

@Composable
fun PantallaUsuario(repository: UsuariosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val users by repository.getUsuario().collectAsState(emptyList())
    PantallaUsuarioContenido(
        usuarioPruebas = users,
        addUser = { scope.launch { repository.addUsuario(it) } },
        updateUser = { scope.launch { repository.updateUsuario(it) } },
        deleteUser = { scope.launch { repository.deleteUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarioContenido(
    usuarioPruebas: List<UsuarioPrueba>,
    addUser: (UsuarioPrueba) -> Unit,
    updateUser: (UsuarioPrueba) -> Unit,
    deleteUser: (UsuarioPrueba) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedUsuarioPrueba by remember { mutableStateOf<UsuarioPrueba?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Usuarios",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaUsuarios(usuarioPruebas = usuarioPruebas, modifier = Modifier.padding(innerPadding)) { user ->
            selectedUsuarioPrueba = user
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoUsuarios(
                    usuarioPrueba = selectedUsuarioPrueba,
                    onSave = { user ->
                        scope.launch {
                            if (selectedUsuarioPrueba == null) {
                                addUser(user)
                            } else {
                                updateUser(user)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuarioPrueba = null
                        }
                    },
                    onDelete = { user ->
                        scope.launch {
                            user?.let { deleteUser(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuarioPrueba = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaUsuarioNuestro(repository: FirestoreUsuariosNuestros, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val usuarios by repository.getUsuario().collectAsState(emptyList())
    PantallaUsuarioNuestroContenido(
        usuarios = usuarios,
        addUsuario = { scope.launch { repository.addUsuario(it) } },
        updateUsuario = { scope.launch { repository.updateUsuario(it) } },
        deleteUsuario = { scope.launch { repository.deleteUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarioNuestroContenido(
    usuarios: List<Usuario>,
    addUsuario: (Usuario) -> Unit,
    updateUsuario: (Usuario) -> Unit,
    deleteUsuario: (Usuario) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedUsuario by remember { mutableStateOf<Usuario?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Usuarios Nuestros",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaUsuariosNuestros(usuarios = usuarios, modifier = Modifier.padding(innerPadding)) { usuario ->
            selectedUsuario = usuario
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoUsuariosNuestros(
                    usuario = selectedUsuario,
                    onSave = { usuario ->
                        scope.launch {
                            if (selectedUsuario == null) {
                                addUsuario(usuario)
                            } else {
                                updateUsuario(usuario)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuario = null
                        }
                    },
                    onDelete = { usuario ->
                        scope.launch {
                            usuario?.let { deleteUsuario(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuario = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaMensaje(repository: MensajesRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val mensajes by repository.getMensaje().collectAsState(emptyList())
    PantallaMensajeContenido(
        mensajes = mensajes,
        addMensaje = { scope.launch { repository.addMensaje(it) } },
        updateMensaje = { scope.launch { repository.updateMensaje(it) } },
        deleteMensaje = { scope.launch { repository.deleteMensaje(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMensajeContenido(
    mensajes: List<Mensaje>,
    addMensaje: (Mensaje) -> Unit,
    updateMensaje: (Mensaje) -> Unit,
    deleteMensaje: (Mensaje) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedMensaje by remember { mutableStateOf<Mensaje?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Mensajes",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaMensajes(mensajes = mensajes, modifier = Modifier.padding(innerPadding)) { mensaje ->
            selectedMensaje = mensaje
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoMensajes(
                    mensaje = selectedMensaje,
                    onSave = { mensaje ->
                        scope.launch {
                            if (selectedMensaje == null) {
                                addMensaje(mensaje)
                            } else {
                                updateMensaje(mensaje)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedMensaje = null
                        }
                    },
                    onDelete = { mensaje ->
                        scope.launch {
                            mensaje?.let { deleteMensaje(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedMensaje = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaConversacion(repository: ConversacionesRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val conversaciones by repository.getConversaciones().collectAsState(emptyList())
    PantallaConversacionContenido(
        conversaciones = conversaciones,
        addConversacion = { scope.launch { repository.addConversacion(it) } },
        updateConversacion = { scope.launch { repository.updateConversacion(it) } },
        deleteConversacion = { scope.launch { repository.deleteConversacion(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConversacionContenido(
    conversaciones: List<Conversacion>,
    addConversacion: (Conversacion) -> Unit,
    updateConversacion: (Conversacion) -> Unit,
    deleteConversacion: (Conversacion) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedConversacion by remember { mutableStateOf<Conversacion?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Conversaciones",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaConversaciones(conversaciones = conversaciones, modifier = Modifier.padding(innerPadding)) { conversacion ->
            selectedConversacion = conversacion
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoConversaciones(
                    conversacion = selectedConversacion,
                    onSave = { conversacion ->
                        scope.launch {
                            if (selectedConversacion == null) {
                                addConversacion(conversacion)
                            } else {
                                updateConversacion(conversacion)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacion = null
                        }
                    },
                    onDelete = { conversacion ->
                        scope.launch {
                            conversacion?.let { deleteConversacion(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacion = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaConversacionUsuario(repository: ConversacionesUsuariosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val conversacionesUsuarios by repository.getConversacionesUsuarios().collectAsState(emptyList())
    PantallaConversacionUsuarioContenido(
        conversacionesUsuarios = conversacionesUsuarios,
        addConversacionUsuario = { scope.launch { repository.addConversacionUsuario(it) } },
        updateConversacionUsuario = { scope.launch { repository.updateConversacionUsuario(it) } },
        deleteConversacionUsuario = { scope.launch { repository.deleteConversacionUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConversacionUsuarioContenido(
    conversacionesUsuarios: List<ConversacionesUsuario>,
    addConversacionUsuario: (ConversacionesUsuario) -> Unit,
    updateConversacionUsuario: (ConversacionesUsuario) -> Unit,
    deleteConversacionUsuario: (ConversacionesUsuario) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedConversacionUsuario by remember { mutableStateOf<ConversacionesUsuario?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Conversaciones Usuarios",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaConversacionesUsuarios(conversacionesUsuarios = conversacionesUsuarios, modifier = Modifier.padding(innerPadding)) { conversacionUsuario ->
            selectedConversacionUsuario = conversacionUsuario
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoConversacionesUsuarios(
                    conversacionesUsuario = selectedConversacionUsuario,
                    onSave = { conversacionUsuario ->
                        scope.launch {
                            if (selectedConversacionUsuario == null) {
                                addConversacionUsuario(conversacionUsuario)
                            } else {
                                updateConversacionUsuario(conversacionUsuario)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacionUsuario = null
                        }
                    },
                    onDelete = { conversacionUsuario ->
                        scope.launch {
                            conversacionUsuario?.let { deleteConversacionUsuario(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacionUsuario = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaPost(repository: PostsRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val posts by repository.getPosts().collectAsState(emptyList())
    PantallaPostContenido(
        posts = posts,
        addPost = { scope.launch { repository.addPost(it) } },
        updatePost = { scope.launch { repository.updatePost(it) } },
        deletePost = { scope.launch { repository.deletePost(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPostContenido(
    posts: List<Post>,
    addPost: (Post) -> Unit,
    updatePost: (Post) -> Unit,
    deletePost: (Post) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Posts",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaPosts(posts = posts, modifier = Modifier.padding(innerPadding)) { post ->
            selectedPost = post
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoPosts(
                    post = selectedPost,
                    onSave = { post ->
                        scope.launch {
                            if (selectedPost == null) {
                                addPost(post)
                            } else {
                                updatePost(post)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedPost = null
                        }
                    },
                    onDelete = { post ->
                        scope.launch {
                            post?.let { deletePost(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedPost = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaHilo(repository: HilosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val hilos by repository.getHilos().collectAsState(emptyList())
    PantallaHiloContenido(
        hilos = hilos,
        addHilo = { scope.launch { repository.addHilo(it) } },
        updateHilo = { scope.launch { repository.updateHilo(it) } },
        deleteHilo = { scope.launch { repository.deleteHilo(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHiloContenido(
    hilos: List<Hilo>,
    addHilo: (Hilo) -> Unit,
    updateHilo: (Hilo) -> Unit,
    deleteHilo: (Hilo) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedHilo by remember { mutableStateOf<Hilo?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Hilos",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaHilos(hilos = hilos, modifier = Modifier.padding(innerPadding)) { hilo ->
            selectedHilo = hilo
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoHilos(
                    hilo = selectedHilo,
                    onSave = { hilo ->
                        scope.launch {
                            if (selectedHilo == null) {
                                addHilo(hilo)
                            } else {
                                updateHilo(hilo)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedHilo = null
                        }
                    },
                    onDelete = { hilo ->
                        scope.launch {
                            hilo?.let { deleteHilo(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedHilo = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaTema(repository: TemasRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val temas by repository.getTemas().collectAsState(emptyList())
    PantallaTemaContenido(
        temas = temas,
        addTema = { scope.launch { repository.addTema(it) } },
        updateTema = { scope.launch { repository.updateTema(it) } },
        deleteTema = { scope.launch { repository.deleteTema(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTemaContenido(
    temas: List<Tema>,
    addTema: (Tema) -> Unit,
    updateTema: (Tema) -> Unit,
    deleteTema: (Tema) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedTema by remember { mutableStateOf<Tema?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Temas",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaTemas(temas = temas, modifier = Modifier.padding(innerPadding)) { tema ->
            selectedTema = tema
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoTemas(
                    tema = selectedTema,
                    onSave = { tema ->
                        scope.launch {
                            if (selectedTema == null) {
                                addTema(tema)
                            } else {
                                updateTema(tema)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedTema = null
                        }
                    },
                    onDelete = { tema ->
                        scope.launch {
                            tema?.let { deleteTema(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedTema = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MuestraObjetosPruebasFriebase(navHostController: NavHostController) {

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
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val tiposObjetos = listOf(
                        "UsuarioPrueba",
                        "Usuario",
                        "Mensaje",
                        "Conversacion",
                        "ConversacionUsuario",
                        "Post",
                        "Hilo",
                        "Tema"
                    )

                    tiposObjetos.forEach {
                        Button(
                            onClick = {
                                navHostController.navigate(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

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
 */