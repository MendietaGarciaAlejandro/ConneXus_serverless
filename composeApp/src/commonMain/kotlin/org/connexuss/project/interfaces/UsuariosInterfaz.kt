package org.connexuss.project.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.usuario.Usuario
import org.jetbrains.compose.ui.tooling.preview.Preview

// Interfaz que muestra los usuarios del grupo, si se hace clic en un usuario, se muestra su perfil
@Composable
@Preview
fun MuestraUsuariosGrupo(
    usuarios: List<Usuario>,
    navController: NavHostController
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("usuarios_grupo"),
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(usuarios) { usuario ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("mostrarPerfil/${usuario.getIdUnicoMio()}")
                                },
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${traducir("nombre_label")} ${usuario.getNombreCompletoMio()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${traducir("alias_label")} ${usuario.getAliasMio()}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${traducir("alias_privado_label")} ${usuario.getAliasPrivadoMio()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
