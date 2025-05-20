package org.connexuss.project.interfaces.ajustes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseRepositorioGenerico
import org.connexuss.project.usuario.*
import org.jetbrains.compose.resources.painterResource
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.avatar
import connexus_serverless.composeapp.generated.resources.unblock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.comun.traducir

@Composable
fun PantallaAjustesControlCuentas(navController: NavHostController) {
    val currentUserId = UsuarioPrincipal?.getIdUnicoMio() ?: return
    val repo = remember { SupabaseRepositorioGenerico() }
    val scope = rememberCoroutineScope()

    var bloqueados by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    LaunchedEffect(Unit) {
        val bloqueos = repo.getAll<UsuarioBloqueado>("usuario_bloqueados").first()
            .filter { it.idUsuario == currentUserId }

        val idsBloqueados = bloqueos.map { it.idBloqueado }
        val todosUsuarios = repo.getAll<Usuario>("usuario").first()
        bloqueados = todosUsuarios.filter { it.idUnico in idsBloqueados }
    }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("ajustes_control_cuentas"),
                navController = navController,
                showBackButton = true,
                muestraEngranaje = true,
                irParaAtras = true
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(traducir("lista_de_cuentas"), style = MaterialTheme.typography.titleLarge)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bloqueados) { usuario ->
                            TarjetaUsuarioBloqueado(
                                usuario = usuario,
                                onDesbloquear = {
                                    scope.launch {
                                        try {
                                            repo.deleteItemMulti<UsuarioBloqueado>(
                                                tableName = "usuario_bloqueados",
                                                conditions = mapOf(
                                                    "idusuario" to currentUserId,
                                                    "idbloqueado" to usuario.idUnico
                                                )
                                            )
                                            bloqueados = bloqueados.filterNot { it.idUnico == usuario.idUnico }
                                            println("Usuario desbloqueado")
                                        } catch (e: Exception) {
                                            println("Error desbloqueando: ${e.message}")
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
}

@Composable
private fun TarjetaUsuarioBloqueado(
    usuario: Usuario,
    onDesbloquear: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE1BEE7)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.avatar),
                    contentDescription = traducir("avatar"),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(usuario.getNombreCompletoMio())
            }

            Icon(
                painter = painterResource(Res.drawable.unblock),
                contentDescription = traducir("desbloquear"),
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onDesbloquear)
            )
        }
    }
}
