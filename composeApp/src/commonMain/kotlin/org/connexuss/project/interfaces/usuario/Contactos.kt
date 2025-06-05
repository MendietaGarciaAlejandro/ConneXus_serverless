package org.connexuss.project.interfaces.usuario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.encriptacion.EncriptacionSimetricaChats
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseSecretosRepo
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UsuarioBloqueado
import org.connexuss.project.usuario.UsuarioContacto
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun muestraContactos(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.idUnico ?: return
    println("🪪 ID usuario actual: $currentUserId")
    val repo = remember { SupabaseRepositorioGenerico() }
    val scope = rememberCoroutineScope()
    val encHelper = remember { EncriptacionSimetricaChats() }
    val secretsRepo = remember { SupabaseSecretosRepo() }

    var registrosContacto by remember { mutableStateOf<List<UsuarioContacto>>(emptyList()) }
    var contactos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var usuariosBloqueados by remember { mutableStateOf<Set<String>>(emptySet()) }

    var showNuevoContactoDialog by remember { mutableStateOf(false) }
    var nuevoContactoId by remember { mutableStateOf("") }

    var showNuevoChatDialog by remember { mutableStateOf(false) }
    var contactosSeleccionados by remember { mutableStateOf<Set<String>>(emptySet()) }
    var nombreGrupo by remember { mutableStateOf("") }

    LaunchedEffect(currentUserId) {
        repo.getAll<UsuarioContacto>("usuario_contacto").collect { lista ->
            registrosContacto = lista.filter { it.idUsuario == currentUserId }
        }
    }

    LaunchedEffect(registrosContacto) {
        val idsDeContactos = registrosContacto.map { it.idContacto }
        if (idsDeContactos.isNotEmpty()) {
            repo.getAll<Usuario>("usuario").collect { usuarios ->
                contactos = usuarios.filter { it.idUnico in idsDeContactos }
            }
        } else {
            contactos = emptyList()
        }
    }

    LaunchedEffect(currentUserId) {
        repo.getAll<UsuarioBloqueado>("usuario_bloqueados").collect { lista ->
            usuariosBloqueados = lista
                .filter { it.idUsuario == currentUserId }
                .map { it.idBloqueado }
                .toSet()
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("contactos"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = false
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(contactos) { usuario ->
                            val estaBloqueado = usuario.idUnico in usuariosBloqueados

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (!estaBloqueado)
                                            Modifier.clickable {
                                                navController.navigate("mostrarPerfilUsuario/${usuario.idUnico}")
                                            }
                                        else Modifier
                                    ),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = if (estaBloqueado)
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${traducir("nombre_label")}: ${usuario.getNombreCompletoMio()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (estaBloqueado)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${traducir("alias_label")}: ${usuario.getAliasMio()}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (estaBloqueado)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (estaBloqueado) {
                                        Text(
                                            text = traducir("bloqueado"),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { showNuevoContactoDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = traducir("nuevo_contacto"))
                        }
                        Button(
                            onClick = { showNuevoChatDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = traducir("nuevo_chat"))
                        }
                    }
                }

                if (showNuevoContactoDialog) {
                    AlertDialog(
                        onDismissRequest = { showNuevoContactoDialog = false },
                        title = { Text(text = traducir("nuevo_contacto")) },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = traducir("introduce_alias_privado_usuario"),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                OutlinedTextField(
                                    value = nuevoContactoId,
                                    onValueChange = { nuevoContactoId = it },
                                    label = { Text(traducir("alias_privado")) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val aliasBuscado = nuevoContactoId.trim()
                                    println("🔍 Buscando usuario con alias privado: $aliasBuscado")

                                    scope.launch {
                                        try {
                                            val todosUsuarios =
                                                repo.getAll<Usuario>("usuario").first()
                                            val usuarioEncontrado =
                                                todosUsuarios.find { it.getAliasPrivadoMio() == aliasBuscado }

                                            if (usuarioEncontrado != null) {
                                                val idEncontrado = usuarioEncontrado.idUnico
                                                if (idEncontrado == currentUserId) {
                                                    println("⚠️ No puedes agregarte a ti mismo como contacto")
                                                    return@launch
                                                }

                                                val nuevoRegistro1 = UsuarioContacto(
                                                    idUsuario = currentUserId,
                                                    idContacto = idEncontrado
                                                )
                                                val nuevoRegistro2 = UsuarioContacto(
                                                    idUsuario = idEncontrado,
                                                    idContacto = currentUserId
                                                )

                                                println("📤 Insertando registros mutuos...")
                                                repo.addItem("usuario_contacto", nuevoRegistro1)
                                                repo.addItem("usuario_contacto", nuevoRegistro2)
                                                println("✅ Contactos agregados")

                                                repo.getAll<UsuarioContacto>("usuario_contacto")
                                                    .collect { lista ->
                                                        registrosContacto =
                                                            lista.filter { it.idUsuario == currentUserId }
                                                    }
                                            } else {
                                                println("❌ No se encontró usuario con ese alias privado")
                                            }
                                        } catch (e: Exception) {
                                            println("❌ Error buscando o agregando contacto: ${e.message}")
                                        }

                                        nuevoContactoId = ""
                                        showNuevoContactoDialog = false
                                    }
                                }
                            ) {
                                Text(text = traducir("guardar"))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                nuevoContactoId = ""
                                showNuevoContactoDialog = false
                            }) {
                                Text(text = traducir("cancelar"))
                            }
                        }
                    )
                }

                if (showNuevoChatDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNuevoChatDialog = false
                            contactosSeleccionados = emptySet()
                            nombreGrupo = ""
                        },
                        title = { Text(text = traducir("crear_nuevo_chat")) },
                        text = {
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    traducir("selecciona_participantes"),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                contactos.forEach { usuario ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Checkbox(
                                            checked = contactosSeleccionados.contains(usuario.idUnico),
                                            onCheckedChange = {
                                                contactosSeleccionados = if (it)
                                                    contactosSeleccionados + usuario.idUnico
                                                else
                                                    contactosSeleccionados - usuario.idUnico
                                            }
                                        )
                                        Text(
                                            text = usuario.getNombreCompletoMio(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                if (contactosSeleccionados.size > 1) {
                                    OutlinedTextField(
                                        value = nombreGrupo,
                                        onValueChange = { nombreGrupo = it },
                                        label = { Text(traducir("nombre_del_grupo")) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                scope.launch {
                                    try {
                                        val participantes = contactosSeleccionados + currentUserId
                                        val nuevaConversacion = encHelper.crearChatSinPadding(
                                            nombrePlain = if (contactosSeleccionados.size > 1) nombreGrupo else null,
                                            secretsRpcRepo = secretsRepo
                                        )

                                        participantes.forEach { idUsuario ->
                                            val relacion = ConversacionesUsuario(
                                                idusuario = idUsuario,
                                                idconversacion = nuevaConversacion.id
                                            )
                                            repo.addItem("conversaciones_usuario", relacion)
                                        }

                                        showNuevoChatDialog = false
                                        contactosSeleccionados = emptySet()
                                        nombreGrupo = ""

                                    } catch (e: Exception) {
                                        println("❌ Error creando nuevo chat: ${e.message}")
                                    }
                                }
                            }) {
                                Text(traducir("crear"))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showNuevoChatDialog = false
                                contactosSeleccionados = emptySet()
                                nombreGrupo = ""
                            }) {
                                Text(traducir("cancelar"))
                            }
                        }
                    )
                }
            }
        }
    }
}
