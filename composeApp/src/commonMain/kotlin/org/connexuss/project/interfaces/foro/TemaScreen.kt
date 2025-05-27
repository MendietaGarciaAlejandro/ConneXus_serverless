package org.connexuss.project.interfaces.foro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.encriptacion.EncriptacionSimetricaForo
import org.connexuss.project.encriptacion.desencriptaTexto
import org.connexuss.project.encriptacion.toHex
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.MiBottomBar
import org.connexuss.project.supabase.ISecretosRepositorio
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseTemasRepositorio
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalStdlibApi::class, ExperimentalEncodingApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun TemaScreen(
    navController: NavHostController,
    temaId: String,
    repoForo: SupabaseRepositorioGenerico,
    secretsRepo: ISecretosRepositorio
) {
    val scope = rememberCoroutineScope()
    var showNewThreadDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val encHelper = remember { EncriptacionSimetricaForo() }

    //var aesKey by remember { mutableStateOf<AES.GCM.Key?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var nombrePlano by remember { mutableStateOf("(cargando tema…)") }

    // Instancia de tu helper
    //val encHelper = remember { EncriptacionSimplificada() }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"

    var claveLista by remember { mutableStateOf(false) }

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

    LaunchedEffect(temaId, tema) {
        if (tema == null) return@LaunchedEffect
        try {
            val secretoRpc = secretsRepo.recuperarSecretoSimpleRpc(temaId)
                ?: throw IllegalStateException("Secreto no disponible")
            // Decodificar clave RAW
            val keyBytes = Base64.decode(secretoRpc.decryptedSecret)
            val aesKey = CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)
            ClaveTemaHolder.clave = aesKey
            claveLista = true

            nombrePlano = aesKey.cipher()
                .decrypt(tema!!.nombre.hexToByteArray())
                .decodeToString()
        } catch (e: Exception) {
            nombrePlano = "(clave no disponible)"
        } finally {
            isLoading = false
        }
    }

    val hilosFlow = remember(temaId, refreshTrigger) {
        repoForo.getAll<Hilo>(tablaHilos)
            .map { list -> list.filter { it.idTema == temaId } }
    }

    val posts by repoForo.getAll<Hilo>("post").collectAsState(initial = emptyList())
    val hilos by hilosFlow.collectAsState(initial = emptyList())

    // Carga inicial
    if (isLoading || tema == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val repoTemas = SupabaseTemasRepositorio()
    val temaBuscado = repoTemas.getTemaPorId(temaId).collectAsState(initial = null).value

    var nombreTemaDesencriptado: String by remember { mutableStateOf("(cargando tema…)") }
    try {
        scope.launch {
            nombreTemaDesencriptado = if (temaBuscado != null && ClaveTemaHolder.clave != null) {
                desencriptaTexto(
                    temaBuscado.nombre,
                    ClaveTemaHolder.clave!!
                )
            } else {
                "(clave o tema no disponible)"
            }
        }
    } catch (e: Exception) {
        nombreTemaDesencriptado = "(clave o tema no disponible)"
    }

    // Si el tema no es nulo, mostrar la lista de hilos
    when {
        tema != null -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            if (temaBuscado != null) {
                                Text(nombreTemaDesencriptado)
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
                                HiloCard(
                                    hilo = hilo,
                                    postCount = posts.count { it.idHilo == hilo.idHilo },
                                ) {
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
                                    try {
                                        val nuevoHilo = encHelper.crearHiloSinPadding(
                                            nombrePlain = titulo,
                                            idTema = temaId,
                                        )
                                        refreshTrigger++
                                        showNewThreadDialog = false
                                    } catch (e: Exception) {
                                        println("Error creando hilo: ${e.message}")
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
