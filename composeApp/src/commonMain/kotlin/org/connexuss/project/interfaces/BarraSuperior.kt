package org.connexuss.project.interfaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.usuarios
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DefaultTopBar(
    title: String,
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = traducir(title),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = traducir("atras"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = {
                    navController?.navigate("ajustes")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = traducir("ajustes"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// Topbar para el grupo en el que se esta chateando,mostrando a la derecha un icono de usuarios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopBarGrupo(
    title: String, // Clave para el título (se usará traducir(title))
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
    onUsuariosClick: () -> Unit = {} // Acción al pulsar sobre el icono de usuarios
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = traducir(title),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = traducir("atras"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = onUsuariosClick) {
                    Icon(
                        painter = painterResource(Res.drawable.usuarios),
                        contentDescription = traducir("usuarios"),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

//TopBar para mostrar el usuario con el que se esta chateando
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopBarUsuario(
    title: String, // Clave para el título (se usará traducir(title))
    profileImage: DrawableResource, // Imagen del usuario
    navController: NavHostController?,
    showBackButton: Boolean = false,
    irParaAtras: Boolean = false,
    muestraEngranaje: Boolean = true,
    onTitleClick: () -> Unit = {} // Acción al pulsar sobre el título
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onTitleClick() }
                    .padding(end = 16.dp)
            ) {
                // Muestra la imagen del usuario con borde y sombra
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    tonalElevation = 2.dp
                ) {
                    Image(
                        painter = painterResource(profileImage),
                        contentDescription = traducir("imagen_perfil"),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Título traducido
                Text(
                    text = traducir(title),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    if (navController != null && irParaAtras) {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = traducir("atras"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (muestraEngranaje) {
                IconButton(onClick = {
                    navController?.navigate("ajustes")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = traducir("ajustes"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
