package org.connexuss.project.interfaces.foro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.encriptacion.EncriptacionSimetricaForo
import org.connexuss.project.supabase.ISecretosRepositorio
import org.connexuss.project.supabase.SupabaseSecretosRepo
import kotlin.io.encoding.ExperimentalEncodingApi

// -----------------------
// Componentes UI reutilizables
// -----------------------
//@Composable
//fun TemaCard(tema: Tema, hilosCount: Int, onTemaClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onTemaClick),
//        elevation = 4.dp,
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = tema.nombre, style = MaterialTheme.typography.h6)
//            Spacer(Modifier.height(4.dp))
//            Text(
//                text = "$hilosCount ${if (hilosCount==1) "hilo" else "hilos"}",
//                style = MaterialTheme.typography.caption
//            )
//        }
//    }
//}

/**
 * Tarjeta para mostrar un Tema cifrado.
 * Obtiene la clave simétrica desde vault.secrets y desencripta el nombre.
 */
@OptIn(ExperimentalStdlibApi::class, ExperimentalEncodingApi::class)
@Composable
fun TemaCard(
    tema: Tema,
    hilosCount: Int,
    onTemaClick: () -> Unit,
    secretsRepo: ISecretosRepositorio = remember { SupabaseSecretosRepo() }
) {
    var nombrePlano by remember { mutableStateOf("(cargando…)") }

    val encHelper = remember { EncriptacionSimetricaForo() }

    val scope = rememberCoroutineScope()

    val secretoRepositorio = remember { SupabaseSecretosRepo() }

    scope.launch {
        encHelper.leerTema(
            temaId = tema.idTema,
            secretsRpcRepo = secretoRepositorio,
        ).let { nombre ->
            nombrePlano = nombre
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTemaClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombrePlano, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$hilosCount ${if (hilosCount==1) "hilo" else "hilos"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Tarjeta para mostrar un hilo cifrado.
 * Carga la clave simétrica desde vault.secrets y desencripta el nombre del hilo.
 */
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HiloCard(
    hilo: Hilo,
    postCount: Int = 0,
    onClick: () -> Unit
) {

    var nombrePlano by remember { mutableStateOf("(cargando…)") }

    val encHelper = remember { EncriptacionSimetricaForo() }

    val scope = rememberCoroutineScope()

    // Desencriptar el nombre del hilo
    scope.launch {
        ClaveTemaHolder.clave?.let {
            encHelper.leerHilo(
                hiloId = hilo.idHilo,
                clave = it
            ).let { nombre ->
                nombrePlano = nombre
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombrePlano, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(
                text = "$postCount ${if (postCount==1) "post" else "posts"}",
                style = MaterialTheme.typography.bodySmall
            )
            //Text(text = "ID Tema: ${hilo.idTema}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Extensión para formatear enteros a dos dígitos (“04”, “12”, etc.) */
private fun Int.twoDigits(): String = this.toString().padStart(2, '0')

/** Extensión en LocalDateTime para el formato "HH:mm dd/MM/yyyy" */
fun LocalDateTime.toFormattedString(): String {
    return "${hour.twoDigits()}:${minute.twoDigits()} " +
            "${dayOfMonth.twoDigits()}/${monthNumber.twoDigits()}/$year"
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun PostItem(post: Post) {

    var nombrePlano by remember { mutableStateOf("(cargando...)") }

    val encHelper = remember { EncriptacionSimetricaForo() }

    val scope = rememberCoroutineScope()

    scope.launch {
        ClaveTemaHolder.clave?.let {
            encHelper.leerPost(
                postId = post.idPost,
                clave = it
            ).let { nombre ->
                nombrePlano = nombre
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = post.aliaspublico, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(text = post.fechaPost.toFormattedString(), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            Text(text = nombrePlano, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun BackButton(navController: NavHostController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Atrás")
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
