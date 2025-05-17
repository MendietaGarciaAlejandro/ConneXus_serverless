package org.connexuss.project.misc

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter

@Composable
actual fun rememberImagePainter(url: String): Painter? {
    return rememberAsyncImagePainter(url)
}

@Composable
actual fun MostrarImagenRemota(url: String) {

}