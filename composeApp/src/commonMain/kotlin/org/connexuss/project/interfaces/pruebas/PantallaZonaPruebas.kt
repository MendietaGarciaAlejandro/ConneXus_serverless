package org.connexuss.project.interfaces.pruebas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.comun.traducir
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun PantallaZonaPruebas(navController: NavHostController) {
    val zonaPruebas = traducir("zona_pruebas")
    val debugTextosRealtime = traducir("debug_textos_realtime")
    val debugPruebasSupabase = traducir("debug_pruebas_supabase")
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = zonaPruebas,
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(zonaPruebas)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("contactos") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("debug_ir_a_contactos"))
                            }
                            Button(
                                onClick = { navController.navigate("ajustesControlCuentas") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(traducir("debug_ajustes_control_cuentas"))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("pruebasTextosRealtime") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(debugTextosRealtime)
                            }
                            Button(
                                onClick = { navController.navigate("supabasePruebas") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(debugPruebasSupabase)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("pruebasEncriptacion") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("debug_pruebas_encriptacion"))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("pruebasPersistencia") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("debug_pruebas_persistencia"))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("zonaReportes") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(traducir("admin_zona_reportes"))
                        }
                    }
                }
            }
        }
    }
}
