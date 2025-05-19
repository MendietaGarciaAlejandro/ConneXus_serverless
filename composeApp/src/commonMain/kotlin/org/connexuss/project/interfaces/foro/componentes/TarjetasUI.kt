package org.connexuss.project.interfaces.foro.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema

@Composable
fun TemaCard(tema: Tema, hilosCount: Int, onTemaClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTemaClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.nombre, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$hilosCount ${if (hilosCount==1) "hilo" else "hilos"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun HiloCard(hilo: Hilo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = hilo.nombre ?: "Hilo sin título", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(text = "ID Tema: ${hilo.idTema}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = post.aliaspublico, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(text = post.fechaPost.toString(), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            Text(text = post.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun BackButton(navController: NavHostController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiloTopBar(
    title: String,
    navController: NavHostController,
    newPostsCount: Int = 0,
    onRefresh: () -> Unit = {},
    showRefresh: Boolean = false,
    startRoute: String = ""
) {
    TopAppBar(
        title = {
            Text(title)
            if (newPostsCount > 0) {
                Badge {
                    Text("+$newPostsCount")
                }
            }
        },
        navigationIcon = { BackButton(navController) },
        actions = {
            if (showRefresh) {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }
        }
    )
}
