package org.connexuss.project.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image.Companion.makeFromEncoded
import java.net.URI

@Composable
actual fun rememberImagePainter(url: String): Painter? {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        image = withContext(Dispatchers.IO) {
            try {
                val connection = URI(url).toURL().openStream()
                val bytes = connection.readBytes()
                makeFromEncoded(bytes).toComposeImageBitmap()

            } catch (e: Exception) {
                println("❌ Error loading image on desktop: ${e.message}")
                null
            }
        }
    }

    return image?.let { BitmapPainter(it) }
}

@Composable
actual fun MostrarImagenRemota(url: String) {
}