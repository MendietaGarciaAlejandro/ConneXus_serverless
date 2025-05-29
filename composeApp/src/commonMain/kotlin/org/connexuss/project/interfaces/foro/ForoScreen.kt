package org.connexuss.project.interfaces.foro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.encriptacion.EncriptacionSimetricaForo
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.MiBottomBar
import org.connexuss.project.supabase.SupabaseSecretosRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForoScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showNewTopicDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val refreshTrigger = remember { mutableStateOf(0) }
    val encHelper = remember { EncriptacionSimetricaForo() }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    val temasFlow = remember(refreshTrigger.value) {
        repoForo.getAll<Tema>(tablaTemas)
            .map { list -> list.filter { it.nombre.contains(searchText, ignoreCase = true) } }
    }

    // Flujos de temas y hilos
    //val temas by repoForo.getAll<Tema>(tablaTemas).collectAsState(initial = emptyList())
    val hilos by repoForo.getAll<Hilo>(tablaHilos).collectAsState(initial = emptyList())

    // Filtrar temas y contar hilos
    val filteredTemas = temasFlow.collectAsState(initial = emptyList()).value

    //val secretsRepo = remember { SupabaseSecretosRepo() }

    fun crearTema(nombre: String) {
        scope.launch {
            try {
                // 1) Llamada simplificada: genera clave, inserta en vault y en temas
                val temaResultado = encHelper.crearTemaSinPadding(
                    nombrePlain  = nombre,
                    secretsRpcRepo = SupabaseSecretosRepo()
                )

                // 2) Refresca UI y muestra notificación
                refreshTrigger.value++
                snackbarHostState.showSnackbar(
                    "Tema '$nombre' creado correctamente",
                    duration = SnackbarDuration.Short
                )
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(
                    "Error al crear tema: ${e.message}",
                    duration = SnackbarDuration.Long
                )
            } finally {
                showNewTopicDialog = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Foro General") },
                actions = {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Buscar...") },
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(150.dp),
                        singleLine = true
                    )
                    IconButton(onClick = { showNewTopicDialog = true }) {
                        Icon(Icons.Rounded.Add, contentDescription = "Nuevo tema")
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
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            LimitaTamanioAncho { modifier ->
                when {
                    filteredTemas.isEmpty() && searchText.isBlank() ->
                        EmptyStateMessage("Presiona el + para crear un nuevo tema")
                    filteredTemas.isEmpty() ->
                        EmptyStateMessage("No se encontraron temas")
                    else -> LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTemas) { tema ->
                            TemaCard(
                                tema = tema,
                                hilosCount = hilos.count { it.idTema == tema.idTema },
                                onTemaClick = { navController.navigate("tema/${tema.idTema}") }
                            )
                        }
                    }
                }
            }

            if (showNewTopicDialog) {
                CrearElementoDialog(
                    title = "Nuevo Tema",
                    label = "Nombre del tema",
                    onDismiss = { showNewTopicDialog = false },
                    onConfirm = { nombre ->
                        crearTema(nombre)
                    }
                )
            }
        }
    }
}
