@file:OptIn(ExperimentalMaterial3Api::class)

package org.connexuss.project.firebase.pruebas

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
import org.connexuss.project.interfaces.DefaultTopBar

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
                HojaContenidoAbajo(
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