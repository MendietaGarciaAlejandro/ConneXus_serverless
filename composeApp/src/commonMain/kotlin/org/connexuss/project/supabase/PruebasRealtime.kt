package org.connexuss.project.supabase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.LimitaTamanioAncho
import kotlin.reflect.KProperty1

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
    currentUserId: Long,
    onSend: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // 1. Listado de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(textos.reversed()) { texto ->
                MessageItem(texto, texto.autor == currentUserId.toString())
            }
        }

        // 2. Campo de entrada y botón de envío
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Escribe un mensaje…") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                )
            )
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onSend(input.trim())
                        input = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar"
                )
            }
        }
    }
}

@Composable
fun MessageItem(texto: Texto, isOwn: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwn) {
            // Avatar simulada
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Burbuja de texto
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isOwn) 16.dp else 0.dp,
                        bottomEnd = if (isOwn) 0.dp else 16.dp
                    )
                )
                .background(
                    if (isOwn)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.secondary
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                Text(
                    text = texto.autor,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = texto.texto,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }

        if (isOwn) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
        }
    }
}

@Composable
fun PantallaTextosRealtime(client: SupabaseClient, currentUserId: Long) {
    val textos by client
        .subscribeTableAsFlow<Texto, Long>("texto", Texto::id)
        .collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LimitaTamanioAncho {
        ChatScreen(
            textos = textos.filter { it.autor.toLong() != currentUserId },
            currentUserId = currentUserId,
            onSend = { nuevoTexto ->
                // lógica para insertar en Supabase
                scope.launch {
                    client.from("texto").insert(
                        mapOf(
                            "autor" to currentUserId,
                            "texto" to nuevoTexto
                        )
                    )
                }
            }
        )
    }
}