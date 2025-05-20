package org.connexuss.project.interfaces.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.whyoleg.cryptography.algorithms.AES
import org.connexuss.project.comunicacion.Mensaje
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

/**
 * Componente que renderiza una burbuja de mensaje.
 *
 * @param mensaje El mensaje a mostrar
 * @param esMio Indica si el mensaje pertenece al usuario actual
 * @param modifier Modificador opcional para personalizar el componente
 * @param nombreRemitente Nombre del remitente para mostrar (opcional, solo para grupos)
 */
@Composable
fun BurbujaMensaje(
    mensaje: Mensaje,
    esMio: Boolean,
    modifier: Modifier = Modifier,
    nombreRemitente: String? = null
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
        mensaje.imageUrl?.let { imageUrl ->
            when {
                esAndroid() || esDesktop() -> {
                    val painter = rememberImagePainter(imageUrl)
                    if (painter != null) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

                esWeb() -> {
                    Box(
                        modifier = Modifier
                            .size(200.dp, 120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("IMAGEN", color = Color.Black)
                    }
                }
            }
        }

        if (mensaje.content.isNotBlank()) {
            Text(mensaje.content)
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
    IconButton(onClick = onClick) {
        Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Enviar")
    }
}
