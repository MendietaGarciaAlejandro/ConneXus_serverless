package org.connexuss.project.misc


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

@Composable
actual fun EnterClickable(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()         // o el tamaño que necesites
            .focusable()           // para recibir foco de teclado
            .onPreviewKeyEvent { ev ->
                if (ev.type == KeyEventType.KeyUp && ev.key == Key.Enter) {
                    onClick()
                    true
                } else false
            }
    ) {
        content()
    }
}