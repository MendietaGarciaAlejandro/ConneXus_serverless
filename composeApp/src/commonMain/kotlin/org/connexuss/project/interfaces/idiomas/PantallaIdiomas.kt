package org.connexuss.project.interfaces.idiomas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.modificadorTamannio.LimitaTamanioAncho
import org.connexuss.project.interfaces.sistema.DefaultTopBar

@Composable
fun PantallaIdiomas(navController: NavHostController) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Idiomas",
                navController = navController,
                showBackButton = true,
                muestraEngranaje = false,
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
                    // Botones para cambiar el idioma
                    Button(
                        onClick = { /* Cambiar idioma a españon */ }
                    ) {
                        Text(
                            text = "Español"
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { /* Cambiar idioma a inglés */ }
                    ) {
                        Text(
                            text = "Inglés"
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { /* Cambiar idioma a portugués */ }
                    ) {
                        Text(
                            text = "Portugués"
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { /* Cambiar idioma a francés */ }
                    ) {
                        Text(
                            text = "Francés"
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { /* Cambiar idioma a alemán */ }
                    ) {
                        Text(
                            text = "Alemán"
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { /* Cambiar idioma a italiano */ }
                    ) {
                        Text(
                            text = "Italiano"
                        )
                    }
                }
            }
        }
    }
}