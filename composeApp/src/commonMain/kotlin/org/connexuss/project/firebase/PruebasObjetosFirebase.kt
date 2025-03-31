package org.connexuss.project.firebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho

@Composable
fun MuestraObjetosPruebasFriebase(navHostController: NavHostController) {

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Objetos Pruebas Firebase",
                navController = navHostController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = true
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
                    val tiposObjetos = listOf(
                        "UsuarioPrueba",
                        "Usuario",
                        "Mensaje",
                        "Conversacion",
                        "ConversacionUsuario",
                        "Post",
                        "Hilo",
                        "Tema"
                    )

                    tiposObjetos.forEach {
                        Button(
                            onClick = {
                                navHostController.navigate(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}