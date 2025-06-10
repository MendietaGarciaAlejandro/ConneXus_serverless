package org.connexuss.project.misc

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.generateId


@Composable
actual fun ChatEnviarImagen(
    chatId: String,
    currentUserId: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            scope.launch {
                val filename = "chat_${chatId}_${System.currentTimeMillis()}.jpg"
                val bucket = Supabase.client.storage.from("imageneschat")

                try {
                    bucket.upload(filename, bytes)
                    val publicUrl = bucket.publicUrl(filename)

                    val mensaje = Mensaje(
                        id = generateId(),
                        content = "",
                        idusuario = currentUserId,
                        idconversacion = chatId,
                        imageUrl = publicUrl,
                        fechaMensaje = null
                    )
                    Supabase.client.from("mensaje").insert(mensaje)

                } catch (e: Exception) {
                    println("❌ Error subiendo imagen: ${e.message}")
                }
            }
        }
    }

    IconButton(onClick = {
        launcher.launch("image/*")
    }) {
        Icon(Icons.Rounded.Add, contentDescription = "Enviar imagen")
    }
}
