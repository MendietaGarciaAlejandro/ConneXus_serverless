package org.connexuss.project.firebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import org.connexuss.project.interfaces.eq
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.usuario.Usuario

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