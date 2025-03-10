package org.connexuss.project

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

// Paleta predeterminada (claro)
val coloresClaros = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta predeterminada (oscuro)
val coloresOscuros = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// Paleta Verde (con buenos contrastes)
val coloresVerde = lightColors(
    primary = Color(0xFF4CAF50),         // Verde
    primaryVariant = Color(0xFF388E3C),
    secondary = Color(0xFF81C784),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFF44336),
    onPrimary = Color.White,             // Texto blanco sobre verde
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Roja
val coloresRojo = lightColors(
    primary = Color(0xFFF44336),         // Rojo
    primaryVariant = Color(0xFFD32F2F),
    secondary = Color(0xFFEF5350),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Azul
val coloresAzul = lightColors(
    primary = Color(0xFF2196F3),         // Azul
    primaryVariant = Color(0xFF1976D2),
    secondary = Color(0xFF64B5F6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFF44336),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Amarilla
val coloresAmarillo = lightColors(
    primary = Color(0xFFFFEB3B),         // Amarillo
    primaryVariant = Color(0xFFFBC02D),
    secondary = Color(0xFFFFF176),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.Black,             // Texto oscuro sobre amarillo
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Naranja
val coloresNaranja = lightColors(
    primary = Color(0xFFFF9800),         // Naranja
    primaryVariant = Color(0xFFF57C00),
    secondary = Color(0xFFFFB74D),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Morada
val coloresMorado = lightColors(
    primary = Color(0xFF9C27B0),         // Morado
    primaryVariant = Color(0xFF7B1FA2),
    secondary = Color(0xFFE1BEE7),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Paleta Gris
val coloresGris = lightColors(
    primary = Color(0xFF9E9E9E),         // Gris
    primaryVariant = Color(0xFF616161),
    secondary = Color(0xFFBDBDBD),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

// Lista de todas las paletas
val ListaCompletaColores = listOf(
    coloresClaros,
    coloresOscuros,
    coloresVerde,
    coloresRojo,
    coloresAzul,
    coloresAmarillo,
    coloresNaranja,
    coloresMorado,
    coloresGris
)