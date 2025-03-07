package org.connexuss.project.interfaces.modificadorTamannio

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
@Composable
fun ResponsiveContent(content: @Composable (Modifier) -> Unit) {
    BoxWithConstraints {
        // Si el ancho máximo disponible es mayor a 600dp, limitamos a 400dp; en caso contrario, usamos fillMaxWidth.
        val modifier = if (maxWidth > 600.dp) {
            Modifier.fillMaxWidth(0.5f) // o Modifier.widthIn(max = 400.dp)
        } else {
            Modifier.fillMaxWidth()
        }
        content(modifier)
    }
}
 */

@Composable
fun LimitaTamanioAncho(content: @Composable (Modifier) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val modifier = if (maxWidth > 600.dp) {
            Modifier.widthIn(max = 800.dp).padding(horizontal = 32.dp) // Aumentamos el ancho máximo y el padding
        } else {
            Modifier.fillMaxWidth()
        }
        content(modifier)
    }
}