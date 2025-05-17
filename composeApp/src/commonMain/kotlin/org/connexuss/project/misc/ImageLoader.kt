package org.connexuss.project.misc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun rememberImagePainter(url: String): Painter?
@Composable
expect fun MostrarImagenRemota(url: String)
