package org.connexuss.project.misc

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun SelectorImagenAndroid(
    onImagenSeleccionada: (ByteArray, nombre: String) -> Unit
) {
    val contexto = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val nombre = uri.lastPathSegment ?: "imagen.jpg"
            val inputStream = contexto.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            if (bytes != null) {
                onImagenSeleccionada(bytes, nombre)
            }
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }
}
