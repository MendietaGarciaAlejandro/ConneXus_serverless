package org.connexuss.project.interfaces.ajustes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.*
import org.connexuss.project.interfaces.modificadorTamannio.LimitaTamanioAncho
import org.connexuss.project.interfaces.sistema.DefaultTopBar
import org.jetbrains.compose.resources.painterResource

// Pantalla de Ajustes / Control de Cuentas
@Composable
fun PantallaAjustesControlCuentas(navController: NavHostController) {
    // Lista de cuentas de ejemplo
    val cuentas = listOf(
        "xi_xin", "marytu5", "gatito47", "pup7", "paap7", "xi_xin"
    )

    MaterialTheme {
        Scaffold(
            topBar = {
                // Ajustes / Control cuentas
                DefaultTopBar(
                    title = "Ajustes / Control cuentas",
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = true,
                    irParaAtras = true
                )
            }
        ) { padding ->
            // Centra el contenido en Desktop/Web
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Limita el ancho en pantallas grandes
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .padding(padding)
                            .padding(16.dp)
                    ) {
                        // Título o descripción adicional (opcional)
                        // Text("Lista de Cuentas", style = MaterialTheme.typography.h6)

                        // Lista con LazyColumn
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(cuentas) { cuenta ->
                                // Cada ítem con fondo lila y un avatar a la izquierda + nombre + ícono a la derecha
                                Card(
                                    backgroundColor = Color(0xFFE1BEE7), // Lila claro
                                    elevation = 2.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Avatar a la izquierda
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Ícono de avatar (reemplaza con tu recurso)
                                            Icon(
                                                painter = painterResource(Res.drawable.avatar),
                                                contentDescription = "Avatar",
                                                modifier = Modifier.size(40.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(cuenta)
                                        }
                                        // Ícono de persona a la derecha
                                        Icon(
                                            painter = painterResource(Res.drawable.ic_person),
                                            contentDescription = "Persona",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Pantalla de Ajustes / Ayuda
@Composable
fun PantallaAjustesAyuda(navController: NavHostController) {
    // Lista de Preguntas y Respuestas de ejemplo
    val faqs = listOf(
        "No puedo agregar a alguien",
        "RE: Haber estudiado",
        "Me han silenciado",
        "RE: le paso por tóxico",
        "Cómo cambio mi alias público",
        "RE: Con la mano",
        "¿Puedo cambiar mi contraseña?",
        "RE: Sí",
        "Vender mis datos",
        "RE: S.. NO, me re"
    )

    // Estado para el reporte que el usuario escriba
    var reporte by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                // Ajustes / Ayuda
                DefaultTopBar(
                    title = "Ajustes / Ayuda",
                    navController = navController,
                    showBackButton = true,
                    muestraEngranaje = true,
                    irParaAtras = true
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .padding(padding)
                            .padding(16.dp)
                    ) {
                        // Lista de Preguntas frecuentes
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), // Para que ocupe el espacio disponible
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(faqs) { pregunta ->
                                Card(
                                    backgroundColor = Color(0xFFD1C4E9), // Lila suave
                                    elevation = 2.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        pregunta,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        // Sección para escribir un reporte
                        Text("Envíe un reporte", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = reporte,
                                onValueChange = { reporte = it },
                                label = { Text("Escribe tu reporte") },
                                modifier = Modifier.weight(1f)
                            )
                            Button(onClick = {
                                // Lógica para enviar el reporte
                                // Ejemplo: se limpia el campo
                                reporte = ""
                            }) {
                                // Ícono de sobre
                                Icon(
                                    painter = painterResource(Res.drawable.ic_email),
                                    contentDescription = "Enviar reporte"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
