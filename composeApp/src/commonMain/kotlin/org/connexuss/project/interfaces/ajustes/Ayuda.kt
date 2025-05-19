package org.connexuss.project.interfaces.ajustes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.misc.Reporte
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.generaIdLongAleatorio
import org.jetbrains.compose.resources.painterResource
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.ic_email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.traducir

@Composable
fun PantallaAjustesAyuda(navController: NavHostController) {
    val faqs = listOf(
        "No puedo agregar a alguien\n\rRE: Haber estudiado",
        "Me han silenciado\n\rRE: Por algo será",
        "Cómo cambio mi alias público\n\rRE: No se puede",
        "¿Puedo cambiar mi contraseña?\n\rRE: Si",
        "Vendeis mis datos\n\rRE: No, bueno, un poco",
    )

    var reporte by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val reporteVacio = traducir("reporte_vacio")
    val reporteEnviado = traducir("reporte_enviado")
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes_ayuda"),
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
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(faqs) { pregunta ->
                            TarjetaFAQ(pregunta = pregunta)
                        }
                    }

                    FormularioReporte(
                        reporte = reporte,
                        onReporteChange = { reporte = it },
                        errorMessage = errorMessage,
                        onEnviarReporte = {
                            errorMessage = if (reporte.isBlank()) {
                                reporteVacio
                            } else {
                                scope.launch {
                                    Supabase.client.from("reporte").insert(
                                        Reporte(
                                            idReporte = generaIdLongAleatorio().toString(),
                                            idUsuario = (UsuarioPrincipal?.getIdUnicoMio() ?: 0).toString(),
                                            motivo = reporte,
                                            resuelto = false,
                                        )
                                    )
                                }
                                reporteEnviado
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaFAQ(pregunta: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD1C4E9)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            pregunta,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun FormularioReporte(
    reporte: String,
    onReporteChange: (String) -> Unit,
    errorMessage: String,
    onEnviarReporte: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(traducir("envia_un_reporte"), style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = reporte,
            onValueChange = onReporteChange,
            label = { Text(traducir("escribe_tu_reporte")) },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onEnviarReporte) {
            Icon(
                painter = painterResource(Res.drawable.ic_email),
                contentDescription = traducir("enviar_reporte"),
                modifier = Modifier.size(24.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    if (errorMessage.isNotEmpty()) {
        Text(
            errorMessage,
            color = if (errorMessage == traducir("reporte_vacio")) Color.Red else Color.Green
        )
    }
}
