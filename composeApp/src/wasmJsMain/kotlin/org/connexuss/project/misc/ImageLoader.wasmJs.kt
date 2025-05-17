package org.connexuss.project.misc

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement

@Composable
actual fun MostrarImagenRemota(url: String) {
    // Obtén el contenedor principal del cuerpo del documento
    val root = document.getElementById("compose-root") as? HTMLDivElement ?: return

    // Crea la imagen y configúrala
    val img: HTMLImageElement = document.createElement("img") as HTMLImageElement
    img.src = url
    img.style.maxWidth = "200px"
    img.style.borderRadius = "8px"
    img.style.marginTop = "8px"
    img.style.marginBottom = "8px"

    // Agrégala al DOM solo si no existe ya una copia (opcional)
    root.appendChild(img)
}

@Composable
actual fun rememberImagePainter(url: String): Painter? {
    TODO("Not yet implemented")
}