@file:OptIn(ExperimentalMaterial3Api::class)

package org.connexuss.project.firebase

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Mensaje
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.usuario.Usuario

@Composable
fun PantallaUsuario(repository: UsuariosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val users by repository.getUsuario().collectAsState(emptyList())
    PantallaUsuarioContenido(
        usuarioPruebas = users,
        addUser = { scope.launch { repository.addUsuario(it) } },
        updateUser = { scope.launch { repository.updateUsuario(it) } },
        deleteUser = { scope.launch { repository.deleteUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarioContenido(
    usuarioPruebas: List<UsuarioPrueba>,
    addUser: (UsuarioPrueba) -> Unit,
    updateUser: (UsuarioPrueba) -> Unit,
    deleteUser: (UsuarioPrueba) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedUsuarioPrueba by remember { mutableStateOf<UsuarioPrueba?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Usuarios",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaUsuarios(usuarioPruebas = usuarioPruebas, modifier = Modifier.padding(innerPadding)) { user ->
            selectedUsuarioPrueba = user
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoUsuarios(
                    usuarioPrueba = selectedUsuarioPrueba,
                    onSave = { user ->
                        scope.launch {
                            if (selectedUsuarioPrueba == null) {
                                addUser(user)
                            } else {
                                updateUser(user)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuarioPrueba = null
                        }
                    },
                    onDelete = { user ->
                        scope.launch {
                            user?.let { deleteUser(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuarioPrueba = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaUsuarioNuestro(repository: FirestoreUsuariosNuestros, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val usuarios by repository.getUsuario().collectAsState(emptyList())
    PantallaUsuarioNuestroContenido(
        usuarios = usuarios,
        addUsuario = { scope.launch { repository.addUsuario(it) } },
        updateUsuario = { scope.launch { repository.updateUsuario(it) } },
        deleteUsuario = { scope.launch { repository.deleteUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarioNuestroContenido(
    usuarios: List<Usuario>,
    addUsuario: (Usuario) -> Unit,
    updateUsuario: (Usuario) -> Unit,
    deleteUsuario: (Usuario) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedUsuario by remember { mutableStateOf<Usuario?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Usuarios Nuestros",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaUsuariosNuestros(usuarios = usuarios, modifier = Modifier.padding(innerPadding)) { usuario ->
            selectedUsuario = usuario
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoUsuariosNuestros(
                    usuario = selectedUsuario,
                    onSave = { usuario ->
                        scope.launch {
                            if (selectedUsuario == null) {
                                addUsuario(usuario)
                            } else {
                                updateUsuario(usuario)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuario = null
                        }
                    },
                    onDelete = { usuario ->
                        scope.launch {
                            usuario?.let { deleteUsuario(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedUsuario = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaMensaje(repository: MensajesRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val mensajes by repository.getMensaje().collectAsState(emptyList())
    PantallaMensajeContenido(
        mensajes = mensajes,
        addMensaje = { scope.launch { repository.addMensaje(it) } },
        updateMensaje = { scope.launch { repository.updateMensaje(it) } },
        deleteMensaje = { scope.launch { repository.deleteMensaje(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMensajeContenido(
    mensajes: List<Mensaje>,
    addMensaje: (Mensaje) -> Unit,
    updateMensaje: (Mensaje) -> Unit,
    deleteMensaje: (Mensaje) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedMensaje by remember { mutableStateOf<Mensaje?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Mensajes",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaMensajes(mensajes = mensajes, modifier = Modifier.padding(innerPadding)) { mensaje ->
            selectedMensaje = mensaje
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoMensajes(
                    mensaje = selectedMensaje,
                    onSave = { mensaje ->
                        scope.launch {
                            if (selectedMensaje == null) {
                                addMensaje(mensaje)
                            } else {
                                updateMensaje(mensaje)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedMensaje = null
                        }
                    },
                    onDelete = { mensaje ->
                        scope.launch {
                            mensaje?.let { deleteMensaje(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedMensaje = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaConversacion(repository: ConversacionesRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val conversaciones by repository.getConversaciones().collectAsState(emptyList())
    PantallaConversacionContenido(
        conversaciones = conversaciones,
        addConversacion = { scope.launch { repository.addConversacion(it) } },
        updateConversacion = { scope.launch { repository.updateConversacion(it) } },
        deleteConversacion = { scope.launch { repository.deleteConversacion(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConversacionContenido(
    conversaciones: List<Conversacion>,
    addConversacion: (Conversacion) -> Unit,
    updateConversacion: (Conversacion) -> Unit,
    deleteConversacion: (Conversacion) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedConversacion by remember { mutableStateOf<Conversacion?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Conversaciones",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaConversaciones(conversaciones = conversaciones, modifier = Modifier.padding(innerPadding)) { conversacion ->
            selectedConversacion = conversacion
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoConversaciones(
                    conversacion = selectedConversacion,
                    onSave = { conversacion ->
                        scope.launch {
                            if (selectedConversacion == null) {
                                addConversacion(conversacion)
                            } else {
                                updateConversacion(conversacion)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacion = null
                        }
                    },
                    onDelete = { conversacion ->
                        scope.launch {
                            conversacion?.let { deleteConversacion(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacion = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaConversacionUsuario(repository: ConversacionesUsuariosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val conversacionesUsuarios by repository.getConversacionesUsuarios().collectAsState(emptyList())
    PantallaConversacionUsuarioContenido(
        conversacionesUsuarios = conversacionesUsuarios,
        addConversacionUsuario = { scope.launch { repository.addConversacionUsuario(it) } },
        updateConversacionUsuario = { scope.launch { repository.updateConversacionUsuario(it) } },
        deleteConversacionUsuario = { scope.launch { repository.deleteConversacionUsuario(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConversacionUsuarioContenido(
    conversacionesUsuarios: List<ConversacionesUsuario>,
    addConversacionUsuario: (ConversacionesUsuario) -> Unit,
    updateConversacionUsuario: (ConversacionesUsuario) -> Unit,
    deleteConversacionUsuario: (ConversacionesUsuario) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedConversacionUsuario by remember { mutableStateOf<ConversacionesUsuario?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Conversaciones Usuarios",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaConversacionesUsuarios(conversacionesUsuarios = conversacionesUsuarios, modifier = Modifier.padding(innerPadding)) { conversacionUsuario ->
            selectedConversacionUsuario = conversacionUsuario
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoConversacionesUsuarios(
                    conversacionesUsuario = selectedConversacionUsuario,
                    onSave = { conversacionUsuario ->
                        scope.launch {
                            if (selectedConversacionUsuario == null) {
                                addConversacionUsuario(conversacionUsuario)
                            } else {
                                updateConversacionUsuario(conversacionUsuario)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacionUsuario = null
                        }
                    },
                    onDelete = { conversacionUsuario ->
                        scope.launch {
                            conversacionUsuario?.let { deleteConversacionUsuario(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedConversacionUsuario = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaPost(repository: PostsRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val posts by repository.getPosts().collectAsState(emptyList())
    PantallaPostContenido(
        posts = posts,
        addPost = { scope.launch { repository.addPost(it) } },
        updatePost = { scope.launch { repository.updatePost(it) } },
        deletePost = { scope.launch { repository.deletePost(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPostContenido(
    posts: List<Post>,
    addPost: (Post) -> Unit,
    updatePost: (Post) -> Unit,
    deletePost: (Post) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Posts",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaPosts(posts = posts, modifier = Modifier.padding(innerPadding)) { post ->
            selectedPost = post
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoPosts(
                    post = selectedPost,
                    onSave = { post ->
                        scope.launch {
                            if (selectedPost == null) {
                                addPost(post)
                            } else {
                                updatePost(post)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedPost = null
                        }
                    },
                    onDelete = { post ->
                        scope.launch {
                            post?.let { deletePost(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedPost = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaHilo(repository: HilosRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val hilos by repository.getHilos().collectAsState(emptyList())
    PantallaHiloContenido(
        hilos = hilos,
        addHilo = { scope.launch { repository.addHilo(it) } },
        updateHilo = { scope.launch { repository.updateHilo(it) } },
        deleteHilo = { scope.launch { repository.deleteHilo(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHiloContenido(
    hilos: List<Hilo>,
    addHilo: (Hilo) -> Unit,
    updateHilo: (Hilo) -> Unit,
    deleteHilo: (Hilo) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedHilo by remember { mutableStateOf<Hilo?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Hilos",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaHilos(hilos = hilos, modifier = Modifier.padding(innerPadding)) { hilo ->
            selectedHilo = hilo
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoHilos(
                    hilo = selectedHilo,
                    onSave = { hilo ->
                        scope.launch {
                            if (selectedHilo == null) {
                                addHilo(hilo)
                            } else {
                                updateHilo(hilo)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedHilo = null
                        }
                    },
                    onDelete = { hilo ->
                        scope.launch {
                            hilo?.let { deleteHilo(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedHilo = null
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaTema(repository: TemasRepositorio, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val temas by repository.getTemas().collectAsState(emptyList())
    PantallaTemaContenido(
        temas = temas,
        addTema = { scope.launch { repository.addTema(it) } },
        updateTema = { scope.launch { repository.updateTema(it) } },
        deleteTema = { scope.launch { repository.deleteTema(it) } },
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTemaContenido(
    temas: List<Tema>,
    addTema: (Tema) -> Unit,
    updateTema: (Tema) -> Unit,
    deleteTema: (Tema) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedTema by remember { mutableStateOf<Tema?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Temas",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        ListaTemas(temas = temas, modifier = Modifier.padding(innerPadding)) { tema ->
            selectedTema = tema
            showBottomSheet = true
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                HojaContenidoAbajoTemas(
                    tema = selectedTema,
                    onSave = { tema ->
                        scope.launch {
                            if (selectedTema == null) {
                                addTema(tema)
                            } else {
                                updateTema(tema)
                            }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedTema = null
                        }
                    },
                    onDelete = { tema ->
                        scope.launch {
                            tema?.let { deleteTema(it) }
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                            selectedTema = null
                        }
                    }
                )
            }
        }
    }
}