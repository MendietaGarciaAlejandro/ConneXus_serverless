package org.connexuss.project.interfaces.idiomas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    // Obtén el estado global del idioma desde el CompositionLocal
    val idiomaState = LocalIdiomaState.current

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("idiomas"),
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
                    val idiomas = listOf(
                        "espanol" to espannol,
                        "ingles" to ingles,
                        "portugues" to portugues,
                        "frances" to frances,
                        "aleman" to aleman,
                        "italiano" to italiano
                    )

                    idiomas.forEach { (clave, idioma) ->
                        Button(
                            onClick = { cambiarIdioma(idiomaState, idioma) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = traducir(clave))
                        }
                    }
                }
            }
        }
    }
}