package org.connexuss.project.supabase

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho
import org.connexuss.project.misc.Reporte
import org.connexuss.project.misc.Supabase
import org.connexuss.project.usuario.Usuario
import kotlin.random.Random
import kotlin.reflect.KProperty1

@Serializable
data class Texto(
    val id: Long,
    val autor: String,
    val texto: String
)

/**
 * Retorna un Flow que:
 * 1. Emite el List<T> inicial de la tabla.
 * 2. Escucha INSERT/UPDATE/DELETE en tiempo real y emite la lista actualizada.
 *
 * @param table       Nombre de la tabla en Postgres.
 * @param primaryKey  Propiedad del DTO que actúa como clave primaria para cache.
 * @param filter      (Opcional) Filtro PostgREST inicial y en cambios.
 * @param channelName (Opcional) Nombre del canal Realtime; por defecto "schema:table:pk".
 *
 * @return Flow<List<T>>
 */
@OptIn(SupabaseExperimental::class)
inline fun <reified T : Any, PK : Any> SupabaseClient.subscribeTableAsFlow(
    table: String,
    primaryKey: KProperty1<T, PK>,
    filter: FilterOperation? = null,
    channelName: String? = null
): Flow<List<T>> = this
    .from(table)
    .selectAsFlow(
        primaryKey  = primaryKey,
        filter      = filter,
        channelName = channelName
    )

@Composable
fun ChatScreen(
    textos: List<Texto>,
    onSend: (autor: String, mensaje: String) -> Unit    // <-- aquí dos parámetros
) {
    var autorInput by remember { mutableStateOf("") }
    var mensajeInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        // 1. Listado de mensajes
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(textos.reversed()) { texto ->
                MessageItem(texto)
            }
        }

        // 2. Inputs y botón
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = autorInput,
                onValueChange = { autorInput = it },
                placeholder = { Text("Tu nombre") },
                modifier = Modifier.weight(0.3f).clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = mensajeInput,
                onValueChange = { mensajeInput = it },
                placeholder = { Text("Escribe un mensaje…") },
                modifier = Modifier.weight(0.6f).clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {
                if (autorInput.isNotBlank() && mensajeInput.isNotBlank()) {
                    onSend(autorInput.trim(), mensajeInput.trim())   // ahora coincide: dos args
                    mensajeInput = ""
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
            }
        }
    }
}

@Composable
fun MessageItem(texto: Texto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start        // Todas las burbujas alineadas a la izquierda :contentReference[oaicite:11]{index=11}
    ) {
        // Burbujas con esquina recortada
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.secondary)
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                Text(
                    text = texto.autor,
                    style = MaterialTheme.typography.caption,    // Usamos caption en Material2 :contentReference[oaicite:12]{index=12}
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = texto.texto,
                    style = MaterialTheme.typography.body1,      // body1 para el contenido del mensaje :contentReference[oaicite:13]{index=13}
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun PantallaTextosRealtime(navHostController: NavHostController) {
//    val client = instanciaSupabaseClient(
//        tieneStorage = true,
//        tieneAuth = true,
//        tieneRealtime = true,
//        tienePostgrest = true
//    )
    val textos by Supabase.client
        .subscribeTableAsFlow<Texto, Long>("texto", Texto::id)
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()               // Para lanzar inserciones :contentReference[oaicite:15]{index=15}

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Chat Tiempo Real",
                navController = navHostController,
                irParaAtras = true,
                showBackButton = true,
                muestraEngranaje = true
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ChatScreen(
                textos = textos,
                onSend = { autor, mensaje ->
                    scope.launch {
                        Supabase.client.from("texto").insert(
                            Texto(
                                id = generaIdLongAleatorio(),
                                autor =  autor,
                                texto =  mensaje
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PantallaReportesRealtime(navHostController: NavHostController) {
    // Suscripción a lista de reportes en tiempo real
    val reportesFlow = remember {
        Supabase.client
            .subscribeTableAsFlow<Reporte, String>("reporte", Reporte::idReporte)
    }
    val reportes by reportesFlow.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Reportes del sistema",
                navController = navHostController,
                irParaAtras = true,
                showBackButton = true,
                muestraEngranaje = true
            )
        }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                ReportesScreen(
                    reportes = reportes,
                    onBuscarUsuario = { idUsuario ->
                        scope.launch {
                            try {
                                // Ejemplo: buscar usuario y manejarlo en la pantalla padre
                                val usuario = SupabaseUsuariosRepositorio()
                                    .getUsuarioPorId(idUsuario)
                                    .first()
                                // Usar usuario si se requiere: analytics, navegación...
                            } catch (e: Exception) {
                                // Manejo de error
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ReportesScreen(
    reportes: List<Reporte>,
    onBuscarUsuario: (String) -> Unit = {}
) {
    var dialogoVisible by remember { mutableStateOf(false) }
    var reporteSeleccionado by remember { mutableStateOf<Reporte?>(null) }
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    val repoUsuario = SupabaseUsuariosRepositorio()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Listado de reportes
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(reportes) { reporte ->
                ReporteItem(
                    reporte = reporte,
                    onClick = {
                        reporteSeleccionado = reporte
                        scope.launch {
                            try {
                                // Obtener el Usuario directamente desde el Flow
                                usuario = repoUsuario
                                    .getUsuarioPorId(reporte.idUsuario)  // Flow<Usuario?>
                                    .first()                            // Usuario?
                            } catch (e: Exception) {
                                usuario = null
                            }
                            onBuscarUsuario(reporte.idUsuario)
                            dialogoVisible = true
                        }
                    }
                )
            }
        }
    }

    // Diálogo con información del reporte y usuario
    if (dialogoVisible && reporteSeleccionado != null) {
        AlertDialog(
            onDismissRequest = {
                dialogoVisible = false
                usuario = null
            },
            title = { Text("Información del Reporte") },
            text = {
                Column {
                    Text("ID Usuario: ${reporteSeleccionado!!.idUsuario}")
//                    usuario?.let {
//                        Text("Nombre: ${it.nombre}")
//                        Text("Correo: ${it.correo}")
//                        Text("Alias Público: ${it.aliasPublico}")
//                        Text("Alias Privado: ${it.aliasPrivado}")
//                        Text("Activo: ${it.activo}")
//                        Text("Descripción: ${it.descripcion}")
//                    } ?: Text("Error al cargar datos del usuario")

                    Text("Motivo del reporte: ${reporteSeleccionado!!.motivo}")
                    //Text("Respuesta Admin: ${reporteSeleccionado!!.respuestaAdmin ?: "Sin respuesta"}")
                    Text("Fecha: ${reporteSeleccionado!!.fecha}")
                }
            },
            confirmButton = {
                Button(onClick = {
                    dialogoVisible = false
                    usuario = null
                }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun ReporteItem(
    reporte: Reporte,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondary.copy(alpha = 0.7f))
            .padding(12.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Start
    ) {
        Column {
            Text(
                text = "ID: ${reporte.idReporte}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = "Usuario: ${reporte.idUsuario}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = reporte.respuestaAdmin ?: "Sin respuesta",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}


fun generaIdLongAleatorio(): Long {
    val largo = Random.nextLong()
    return largo
}