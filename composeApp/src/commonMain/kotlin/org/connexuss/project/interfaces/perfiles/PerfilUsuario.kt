package org.connexuss.project.interfaces.perfiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.visibilidadOff
import connexus_serverless.composeapp.generated.resources.visibilidadOn
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.misc.Imagen
import org.connexuss.project.misc.Supabase
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.misc.obtenerClaveDesdeImagen
import org.connexuss.project.misc.obtenerImagenAleatoria
import org.connexuss.project.misc.obtenerImagenDesdeId
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio
import org.connexuss.project.usuario.Usuario
import org.connexuss.project.usuario.UsuarioBloqueado
import org.connexuss.project.usuario.UsuarioContacto
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val repo = SupabaseUsuariosRepositorio()

/**
 * Composable que muestra el perfil del usuario.
 *
 * @param navController controlador de navegación.
 * @param usuarioU usuario a mostrar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun mostrarPerfil(navController: NavHostController, usuarioU: Usuario?) {
    // Se recibe el usuario
    //val usuario = usuarioU

    //variable para actualizar datos
    val coroutineScope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(Unit) {
        try {
            val repo = SupabaseUsuariosRepositorio()
            val uid = Supabase.client.auth.currentUserOrNull()?.id
            println("🪪 UID actual autenticado: $uid")

            usuario = repo.getUsuarioAutenticado()
            val usuarioEncontrado = repo.getUsuarioAutenticado()
            println("🧠 Usuario encontrado en la tabla: $usuarioEncontrado")

        } catch (e: Exception) {
            println("Error cargando usuario autenticado: ${e.message}")

        }
    }


    // Dialogs
    var showDialogNombre by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }

    // Campos del usuario
    var aliasPrivado by remember { mutableStateOf("") }
    var aliasPublico by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var contrasennia by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isNameVisible by remember { mutableStateOf(false) }

    //Actualiza los campos del usuario al cargar la pantalla
    LaunchedEffect(usuario) {
        usuario?.let {
            aliasPrivado = it.getAliasPrivadoMio()
            aliasPublico = it.getAliasMio()
            descripcion = it.getDescripcionMio()
            contrasennia = it.getContrasenniaMio()
            email = it.getCorreoMio()
        }
    }

    val imagenPerfilState = remember(usuario) {
        mutableStateOf(obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio()))
    }

    LaunchedEffect(usuario?.getImagenPerfilIdMio()) {
        imagenPerfilState.value = obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio())
    }


    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = traducir("perfil")) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = traducir("volver")
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LimitaTamanioAncho { modifier ->
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (usuario == null) {
                            Text(
                                text = traducir("usuario_no_encontrado"),
                                style = MaterialTheme.typography.titleLarge
                            )
                        } else {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier.size(120.dp),
                                tonalElevation = 4.dp,
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            ) {
                                Image(
                                    painter = painterResource(imagenPerfilState.value),
                                    contentDescription = "Imagen de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            FilledTonalButton(
                                onClick = {
                                    val nuevaImagen = obtenerImagenAleatoria()
                                    imagenPerfilState.value = nuevaImagen
                                    usuario?.apply {
                                        setImagenPerfilMia(nuevaImagen)
                                        setImagenPerfilIdMia(obtenerClaveDesdeImagen(nuevaImagen))
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = traducir("cambiar"))
                            }

                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Alias
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = aliasPrivado,
                                            onValueChange = { aliasPrivado = it },
                                            label = { Text(text = traducir("alias_privado")) },
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                        OutlinedTextField(
                                            value = aliasPublico,
                                            onValueChange = { aliasPublico = it },
                                            label = { Text(text = traducir("alias_publico")) },
                                            modifier = Modifier.weight(1f),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                    }

                                    // Descripción
                                    OutlinedTextField(
                                        value = descripcion,
                                        onValueChange = { descripcion = it },
                                        label = { Text(text = traducir("descripcion")) },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 3,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    // Contraseña
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = contrasennia,
                                            onValueChange = { },
                                            label = { Text(text = traducir("nombre_label")) },
                                            modifier = Modifier.weight(1f),
                                            readOnly = true,
                                            visualTransformation = if (isNameVisible)
                                                VisualTransformation.None
                                            else
                                                PasswordVisualTransformation(),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                        IconButton(
                                            onClick = { isNameVisible = !isNameVisible }
                                        ) {
                                            Icon(
                                                painter = if (isNameVisible)
                                                    painterResource(Res.drawable.visibilidadOn)
                                                else
                                                    painterResource(Res.drawable.visibilidadOff),
                                                contentDescription = if (isNameVisible)
                                                    traducir("ocultar_nombre")
                                                else
                                                    traducir("mostrar_nombre")
                                            )
                                        }
                                        FilledTonalButton(
                                            onClick = {
                                                nuevoNombre = contrasennia
                                                showDialogNombre = true
                                            }
                                        ) {
                                            Text(text = traducir("modificar"))
                                        }
                                    }

                                    // Email
                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = {},
                                        label = { Text(text = traducir("email")) },
                                        readOnly = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = {
                                        usuario?.apply {
                                            setAliasPrivadoMio(aliasPrivado)
                                            setAliasMio(aliasPublico)
                                            setDescripcionMio(descripcion)
                                            setContrasenniaMio(contrasennia)
                                            setCorreoMio(email)
                                        }

                                        usuario?.let {
                                            coroutineScope.launch {
                                                try {
                                                    if (contrasennia != it.getContrasenniaMio()) {
                                                        Supabase.client.auth.updateUser {
                                                            password = contrasennia
                                                        }
                                                    }
                                                    repo.updateUsuario(it)
                                                    usuario = repo.getUsuarioAutenticado()
                                                    navController.popBackStack()
                                                } catch (e: Exception) {
                                                    println("Error al actualizar usuario: ${e.message}")
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = traducir("aplicar"))
                                }
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = traducir("cancelar"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showDialogNombre) {
        AlertDialog(
            onDismissRequest = { showDialogNombre = false },
            title = {
                Text(
                    text = traducir("modificar_nombre"),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text(text = traducir("nuevo_nombre")) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        contrasennia = nuevoNombre
                        usuario?.setContrasenniaMio(nuevoNombre)
                        showDialogNombre = false

                        usuario?.let {
                            coroutineScope.launch {
                                try {
                                    Supabase.client.auth.updateUser {
                                        password = nuevoNombre
                                    }
                                    repo.updateUsuario(it)
                                } catch (e: Exception) {
                                    println("❌ Error al cambiar contraseña: ${e.message}")
                                }
                            }
                        }
                    }
                ) {
                    Text(text = traducir("guardar"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialogNombre = false }
                ) {
                    Text(text = traducir("cancelar"))
                }
            }
        )
    }
}

//Mostrar perfil usuario chat
/**
 * Composable que muestra el perfil de un usuario en el chat.
 *
 * @param navController controlador de navegación.
 * @param userId identificador único del usuario.
 * @param imagenesApp lista de imágenes de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun mostrarPerfilUsuario(
    navController: NavHostController,
    userId: String?,
    imagenesApp: List<Imagen>
) {
    val scope = rememberCoroutineScope()
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(userId) {
        if (userId == null) return@LaunchedEffect
        val todosUsuarios = repo.getAll<Usuario>("usuario").first()
        usuario = todosUsuarios.find { it.getIdUnicoMio() == userId }
        println("🙋 Usuario cargado: ${usuario?.getNombreCompletoMio()}")
    }
    if (usuario == null) return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = usuario?.getNombreCompletoMio() ?: "Perfil",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        if (usuario == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Usuario no encontrado",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            val aliasPrivado by remember { mutableStateOf(usuario?.getAliasPrivadoMio() ?: "") }
            val aliasPublico by remember { mutableStateOf(usuario?.getAliasMio() ?: "") }
            val descripcion by remember { mutableStateOf(usuario?.getDescripcionMio() ?: "") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imagenDrawable = remember(usuario?.getImagenPerfilIdMio()) {
                    obtenerImagenDesdeId(usuario?.getImagenPerfilIdMio())
                }

                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tonalElevation = 4.dp,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ) {
                    Image(
                        painter = painterResource(imagenDrawable),
                        contentDescription = "Imagen de perfil de ${usuario?.getNombreCompletoMio()}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = aliasPrivado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Alias Privado") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        OutlinedTextField(
                            value = aliasPublico,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Alias Público") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            if (usuario != null) {
                                scope.launch {
                                    val repo = SupabaseRepositorioGenerico()
                                    try {
                                        val relaciones =
                                            repo.getAll<ConversacionesUsuario>("conversaciones_usuario")
                                                .first()
                                        val conversacionesComunes = relaciones
                                            .groupBy { it.idconversacion }
                                            .filter { (_, users) ->
                                                val ids = users.map { it.idusuario }
                                                currentUserId in ids && usuario!!.getIdUnicoMio() in ids && ids.size == 2
                                            }
                                            .keys

                                        println("🔍 Conversaciones individuales encontradas: $conversacionesComunes")

                                        for (convId in conversacionesComunes) {
                                            repo.deleteItem<ConversacionesUsuario>(
                                                tableName = "conversaciones_usuario",
                                                idField = "idconversacion",
                                                idValue = convId
                                            )
                                            repo.deleteItem<Conversacion>(
                                                tableName = "conversacion",
                                                idField = "id",
                                                idValue = convId
                                            )
                                            println("🗑️ Eliminada conversación $convId")
                                        }

                                        repo.deleteItemMulti<UsuarioContacto>(
                                            tableName = "usuario_contacto",
                                            conditions = mapOf(
                                                "idusuario" to currentUserId,
                                                "idcontacto" to usuario!!.getIdUnicoMio()
                                            )
                                        )

                                        repo.deleteItemMulti<UsuarioContacto>(
                                            tableName = "usuario_contacto",
                                            conditions = mapOf(
                                                "idusuario" to usuario!!.getIdUnicoMio(),
                                                "idcontacto" to currentUserId
                                            )
                                        )

                                        usuario = null
                                        navController.popBackStack()
                                        return@launch

                                    } catch (e: Exception) {
                                        println("❌ Error eliminando contacto: ${e.message}")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar Contacto")
                    }

                    FilledTonalButton(
                        onClick = {
                            if (usuario != null) {
                                scope.launch {
                                    try {
                                        val nuevoBloqueo = UsuarioBloqueado(
                                            idUsuario = currentUserId,
                                            idBloqueado = usuario!!.getIdUnicoMio()
                                        )
                                        repo.addItem("usuario_bloqueados", nuevoBloqueo)
                                        println("🚫 Usuario bloqueado correctamente")
                                        navController.popBackStack()
                                    } catch (e: Exception) {
                                        println("❌ Error al bloquear usuario: ${e.message}")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Bloquear Usuario")
                    }
                }
            }
        }
    }
}
