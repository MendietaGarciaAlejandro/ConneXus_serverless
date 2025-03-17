package org.connexuss.project.firebase.pruebas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListaUsuarios(usuarioPruebas: List<UsuarioPrueba>, modifier: Modifier = Modifier, userClicked: (UsuarioPrueba) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(usuarioPruebas, key = { it.id }) { user ->
            ItemUsuario(usuarioPrueba = user) {
                userClicked(user)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}