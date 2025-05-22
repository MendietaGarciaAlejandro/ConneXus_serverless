package org.connexuss.project.interfaces.foro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.encriptacion.EncriptacionSimetricaForo
import org.connexuss.project.encriptacion.desencriptaTexto
import org.connexuss.project.encriptacion.toHex
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.MiBottomBar
import org.connexuss.project.interfaces.notificaciones.HiloState
import org.connexuss.project.interfaces.notificaciones.HiloTopBar
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseHiloRepositorio

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HiloScreen(navController: NavHostController, hiloId: String, startRoute: String) {

    val hiloState = remember(hiloId) { HiloState(hiloId) }
    val scope = rememberCoroutineScope()

    // Inicia la escucha al entrar en pantalla
    DisposableEffect(hiloState) {
        onDispose { scope.launch { hiloState.stop() } }
    }

    var contenido by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableStateOf(0) }
    val encHelper = remember { EncriptacionSimetricaForo() }

    // Tablas de temas y hilos
    val tablaTemas = "tema"
    val tablaHilos = "hilo"
    val tablaPosts = "post"

    var nombrePlano by remember { mutableStateOf("(cargando tema…)") }
    var claveLista by remember { mutableStateOf(false) }

    // Creamos el Flow dentro de un remember que observe el trigger
    val postsFlow = remember(hiloId, refreshTrigger) {
        repoForo
            .getAll<Post>(tablaPosts)
            .map { list -> list.filter { it.idHilo == hiloId } }
    }

    // Flujo de hilo (opcionalmente para título) y posts
    val hilo by repoForo.getItem<Hilo>(tablaHilos) {
        scope.launch {
            select {
                filter { eq("idhilo", hiloId) }
            }
        }
    }.collectAsState(initial = null)

    // Recogemos los posts del hilo
    val posts by postsFlow.collectAsState(initial = emptyList())

    if (hilo == null) {
        EmptyStateMessage("Hilo no encontrado")
        return
    }

    val repoForoDedicado = SupabaseHiloRepositorio()
    val hiloBuscado = repoForoDedicado.getHiloPorId(hiloId).collectAsState(initial = null).value

    var nombreDesencriptado: String by remember { mutableStateOf("") }

    scope.launch {
        if (hiloBuscado != null) {
            ClaveTemaHolder.clave?.let {
                encHelper.leerHilo(
                    hiloId = hiloBuscado.idHilo,
                    clave = it
                ).let { nombre ->
                    nombreDesencriptado = nombre
                }
            }
        }
    }

    LaunchedEffect(hiloId, hilo) {
        if (hilo == null) return@LaunchedEffect
        nombrePlano = try {
            hilo!!.nombre?.let {
                ClaveTemaHolder.clave?.cipher()
                    ?.decrypt(it.hexToByteArray())
                    ?.decodeToString()
            } ?: "(clave no disponible)"
        } catch (e: Exception) {
            "(clave no disponible)"
        } finally {
            claveLista = false
        }
    }

    Scaffold(
        topBar = {
            if (hiloBuscado != null) {
                HiloTopBar(
                    title = nombreDesencriptado,
                    navController = navController,
                    newPostsCount = hiloState.newPostsCount.value,
                    onRefresh = { hiloState.reset(); refreshTrigger++ },
                    showRefresh = true,
                    startRoute = startRoute
                )
            }
        },
        bottomBar = { MiBottomBar(navController) }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        PostItem(post = post)
                    }
                }
                HorizontalDivider()
                // Sección para nuevo post
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = contenido,
                        onValueChange = { contenido = it },
                        label = { Text("Nuevo mensaje") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        try {
                            require(
                                contenido.isNotBlank().or(contenido.isNotEmpty())
                            )
                            scope.launch {

                                val nuevoPost = UsuarioPrincipal?.let {
                                    encHelper.crearPostSinPadding(
                                        nombrePlain = contenido,
                                        idHilo = hiloId,
                                        aliaspublico = it.aliasPublico,
                                        idFirmante = UsuarioPrincipal!!.idUnico,
                                    )
                                }

                                refreshTrigger++ // Incrementamos el trigger para refrescar la lista
                                contenido = ""

                            }
                        } catch (e: Exception) {
                            println("Error al enviar el post: ${e.message}")
                        }
                    }) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

