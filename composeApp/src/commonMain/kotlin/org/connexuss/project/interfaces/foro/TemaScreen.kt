package org.connexuss.project.interfaces.foro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.foro.componentes.BackButton
import org.connexuss.project.interfaces.foro.componentes.CrearElementoDialog
import org.connexuss.project.interfaces.foro.componentes.HiloCard
import org.connexuss.project.interfaces.navegacion.MiBottomBar
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseTemasRepositorio

// Repositorio genérico compartido (mismo que en ForoScreen)
private val repoForo = SupabaseRepositorioGenerico()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemaScreen(navController: NavHostController, temaId: String) {
    val scope = rememberCoroutineScope()
    var showNewThreadDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    // Flujo del tema y flujo de hilos filtrados
    val tema by repoForo
        .getItem<Tema>(tablaTemas) {
            scope.launch {
                select {
                    filter { eq("idtema", temaId) }
                }
            }
        }
        .collectAsState(initial = null)

    val hilosFlow = remember(temaId, refreshTrigger) {
        repoForo.getAll<Hilo>(tablaHilos)
            .map { list -> list.filter { it.idTema == temaId } }
    }
    val hilos by hilosFlow.collectAsState(initial = emptyList())

    // Si el tema es nulo, mostrar un indicador de carga
    when {
        tema == null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    val repoTemas = SupabaseTemasRepositorio()
    val temaBuscado = repoTemas.getTemaPorId(temaId).collectAsState(initial = null).value

    // Si el tema no es nulo, mostrar la lista de hilos
    when {
        tema != null -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            if (temaBuscado != null) {
                                Text(temaBuscado.nombre)
                            }
                        },
                        navigationIcon = { BackButton(navController) },
                        actions = {
                            IconButton(onClick = { showNewThreadDialog = true }) {
                                Icon(Icons.Rounded.Add, contentDescription = "Nuevo hilo")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                bottomBar = { MiBottomBar(navController) }
            ) { padding ->
                LimitaTamanioAncho { modifier ->
                    if (tema == null) {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tema no encontrado")
                        }
                    } else {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(padding)
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(hilos) { hilo ->
                                HiloCard(hilo = hilo) {
                                    navController.navigate("hilo/${hilo.idHilo}")
                                }
                            }
                        }
                    }

                    if (showNewThreadDialog && tema != null) {
                        CrearElementoDialog(
                            title = "Nuevo Hilo",
                            label = "Título del hilo",
                            onDismiss = { showNewThreadDialog = false },
                            onConfirm = { titulo ->
                                scope.launch {
                                    val nuevo = Hilo(idHilo = generateId(), nombre = titulo, idTema = temaId)
                                    repoForo.addItem(tablaHilos, nuevo)
                                    refreshTrigger++ // Incrementamos el trigger para refrescar la lista
                                    showNewThreadDialog = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
