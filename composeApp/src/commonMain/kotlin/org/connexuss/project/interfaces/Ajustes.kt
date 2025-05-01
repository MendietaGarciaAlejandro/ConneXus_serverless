package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.ic_email
import connexus_serverless.composeapp.generated.resources.unblock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UsuarioBloqueado
import org.jetbrains.compose.resources.painterResource

/**
 * Muestra la pantalla de control de cuentas (Ajustes).
 *
 * Genera una lista de cuentas de ejemplo y las muestra en tarjetas dentro de un LazyColumn.
 *
 * @param navController Controlador de navegación para gestionar la navegación en la aplicación.
 */@Composable
fun PantallaAjustesControlCuentas(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    val scope = rememberCoroutineScope()

    var bloqueados by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Paso 1: Obtener bloqueos
        val bloqueos = repo.getAll<UsuarioBloqueado>("usuario_bloqueados").first()
            .filter { it.idUsuario == currentUserId }

        val idsBloqueados = bloqueos.map { it.idBloqueado }

        // Paso 2: Obtener usuarios bloqueados
        val todosUsuarios = repo.getAll<Usuario>("usuario").first()
        bloqueados = todosUsuarios.filter { it.idUnico in idsBloqueados }
    }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes_control_cuentas"),
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
                    Text(traducir("lista_de_cuentas"), style = MaterialTheme.typography.h6)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bloqueados) { usuario ->
                            Card(
                                backgroundColor = Color(0xFFE1BEE7),
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(Res.drawable.avatar),
                                            contentDescription = traducir("avatar"),
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(usuario.getNombreCompletoMio())
                                    }

                                    // Desbloquear
                                    Icon(
                                        painter = painterResource(Res.drawable.unblock),
                                        contentDescription = traducir("desbloquear"),
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                scope.launch {
                                                    try {
                                                        repo.deleteItemMulti<UsuarioBloqueado>(
                                                            tableName = "usuario_bloqueados",
                                                            conditions = mapOf(
                                                                "idusuario" to currentUserId,
                                                                "idbloqueado" to usuario.idUnico
                                                            )
                                                        )
                                                        bloqueados = bloqueados.filterNot { it.idUnico == usuario.idUnico }
                                                        println("Usuario desbloqueado")
                                                    } catch (e: Exception) {
                                                        println("Error desbloqueando: ${e.message}")
                                                    }
                                                }
                                            }
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


/**
 * Muestra la pantalla de Ayuda (Ajustes).
 *
 * Presenta una lista de preguntas frecuentes y permite enviar reportes.
 * Gestiona estados para mostrar mensajes de validación y el estado del reporte.
 *
 * @param navController Controlador de navegación para gestionar la navegación en la aplicación.
 */
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

    MaterialTheme {
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
                            //.verticalScroll(rememberScrollState())
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(faqs) { pregunta ->
                                Card(
                                    backgroundColor = Color(0xFFD1C4E9),
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
                        Text(traducir("envia_un_reporte"), style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = reporte,
                                onValueChange = { reporte = it },
                                label = { Text(traducir("escribe_tu_reporte")) },
                                modifier = Modifier.weight(1f)
                            )
                            Button(onClick = {
                                errorMessage = if (reporte.isBlank()) {
                                    reporteVacio
                                } else {
                                    reporteEnviado
                                }
                            }) {
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
                }
            }
        }
    }
}

/**
 * Genera una lista de usuarios aleatorios.
 *
 * Retorna una lista de 100 nombres de usuario simulados, en formato "Usuario {n}".
 *
 * @return Lista de nombres de usuario.
 */
@Composable
fun generaUsuariosAleatorios(): List<String> {
    val usuariosGenerados = mutableListOf<String>()
    for (i in 1..100) {
        usuariosGenerados.add("Usuario $i")
    }
    return usuariosGenerados
}