@file:OptIn(ExperimentalMaterial3Api::class)


package org.connexuss.project.firebase.pruebas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HojaContenidoAbajo(
    usuarioPrueba: UsuarioPrueba? = null,
    onSave: (UsuarioPrueba) -> Unit,
    onDelete: (UsuarioPrueba?) -> Unit
) {
    var nombre by remember { mutableStateOf(usuarioPrueba?.name ?: "") }
    var titulo by remember { mutableStateOf(usuarioPrueba?.title ?: "") }
    var compannia by remember { mutableStateOf(usuarioPrueba?.company ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nombre,
            onValueChange = { nombre = it },
            singleLine = true,
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = titulo,
            onValueChange = { titulo = it },
            singleLine = true,
            label = { Text("Título") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = compannia,
            onValueChange = { compannia = it },
            singleLine = true,
            label = { Text("Compañía") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onSave(UsuarioPrueba(usuarioPrueba?.id ?: "", nombre, titulo, compannia)) }) {
                Text(text = if (usuarioPrueba == null) "Guardar" else "Actualizar")
            }
            Button(onClick = { onDelete(usuarioPrueba) }) {
                Text("Borrar")
            }
        }
    }
}