package org.connexuss.project.interfaces.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.whyoleg.cryptography.algorithms.AES
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.encriptacion.EncriptacionSimetricaChats
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.foro.toFormattedString
import org.connexuss.project.interfaces.foro.toFormattedStringSmall
import org.connexuss.project.misc.esAndroid
import org.connexuss.project.misc.esDesktop
import org.connexuss.project.misc.esWeb
import org.connexuss.project.misc.rememberImagePainter

object ClaveSimetricaChats {
    var clave: AES.GCM.Key? by mutableStateOf(null)
}

/**
 * Componentes y utilidades comunes para interfaces de chat en la aplicación.
 * Contiene funciones de utilidad que son comunes a los diferentes tipos de chat.
 */

/**
 * Componente que muestra un mensaje de carga mientras se inicializa un chat.
 */
@Composable
fun ChatLoading(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MensajeCard(
    mensaje: Mensaje,
    esMio: Boolean,
    modifier: Modifier = Modifier,
    senderAlias: String? = null
) {
    val cargando = traducir("cargando")

    var nombrePlano by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val escHelper = remember { EncriptacionSimetricaChats() }

//    LaunchedEffect(ClaveSimetricaChats.clave, mensaje.content) {
//        if (ClaveSimetricaChats.clave != null) {
//            val cipherBytes = mensaje.content.hexToByteArray()
//            val plainBytes = cipherBytes.let {
//                ClaveSimetricaChats.clave!!.cipher().decrypt(ciphertext = it)
//            }
//            nombrePlano = plainBytes.decodeToString()
//        }
//    }

    // Desencriptar el mensaje usando la clave simétrica
    if (mensaje.content.isNotBlank()) {
        scope.launch {
            ClaveSimetricaChats.clave?.let {
                escHelper.leerMensaje(
                    mensajeId = mensaje.id,
                    clave = it
                ).let { nombre ->
                    nombrePlano = nombre
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = modifier
                .background(
                    if (esMio) Color(0xFFC8E6C9) else Color(0xFFB2EBF2),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            if (!esMio) {
                if (senderAlias != null) {
                    Text(
                        text = senderAlias,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }

            // Primero, el texto (si lo hay)
            if (nombrePlano.isNotEmpty()) {
                Text(nombrePlano)
                Spacer(Modifier.height(4.dp))
            }

            // Luego, la imagen (si viene)
            mensaje.imageUrl?.let { url ->
                ImageMensaje(url)
            }

            // Abajo a la derecha en muy pequeñito se mostrará la fecha del mensaje
            mensaje.fechaMensaje?.let {
                Text(
                    text = it.toFormattedStringSmall(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ImageMensaje(url: String) {
    val imagen = traducir("imagen")
    if (esAndroid() || esDesktop()) {
        val painter = rememberImagePainter(url)
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    } else if (esWeb()) {
        Box(
            modifier = Modifier
                .size(200.dp, 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(imagen, color = Color.Black)
        }
    } else {
        // Manejo por defecto si no es Android, Desktop o Web
        Box(
            modifier = Modifier
                .size(200.dp, 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(imagen, color = Color.Black)
        }
    }
}

/**
 * Componente de botón para enviar mensajes.
 *
 * @param onClick Acción a realizar cuando se hace clic en el botón
 */
@Composable
fun BotonEnviarMensaje(onClick: () -> Unit) {
    val enviarMensaje = traducir("enviar_mensaje")
    IconButton(onClick = onClick) {
        Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = enviarMensaje)
    }
}
