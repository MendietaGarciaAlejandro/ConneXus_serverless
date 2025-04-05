package org.connexuss.project.supabase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho

// Inicializa el cliente de Supabase con tus credenciales
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://riydmqawtpwmulqlbbjq.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJpeWRtcWF3dHB3bXVscWxiYmpxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDM2MjIyNTksImV4cCI6MjA1OTE5ODI1OX0.ShUPbRe_6yvIT27o5S7JE8h3ErIJJo-icrdQD1ugl8o",
) {
    install(Storage)
    //install(Auth)
    install(Realtime)
    install(Postgrest)
}

@Composable
fun SupabaseUsuariosCRUD(navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    // Variable para almacenar la lista de usuarios
    var usuarios by remember { mutableStateOf(emptyList<Supausuario>()) }
    // Variables para el formulario de inserción
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Variables para editar
    var showEditDialog by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<Supausuario?>(null) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoEmail by remember { mutableStateOf("") }
    var nuevoPassword by remember { mutableStateOf("") }

    // Función para recargar usuarios
    suspend fun cargarUsuarios() {
        usuarios = supabaseClient
            .from("supausuarios")
            .select()
            .decodeList<Supausuario>()
    }

    // Cargar usuarios al iniciar el Composable
    LaunchedEffect(Unit) {
        cargarUsuarios()
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "CRUD de Usuarios",
                    navController = navHostController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(modifier = modifier.fillMaxSize().padding(padding)) {
                    Text("Supausuarios registrados", style = MaterialTheme.typography.h6)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(usuarios) { usuario ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${usuario.nombre}, ${usuario.email}",
                                    style = MaterialTheme.typography.body1
                                )
                                Row {
                                    Button(
                                        onClick = {
                                            // Preparamos los campos para editar
                                            usuarioAEditar = usuario
                                            nuevoNombre = usuario.nombre
                                            nuevoEmail = usuario.email
                                            nuevoPassword = usuario.password
                                            showEditDialog = true
                                        },
                                        modifier = Modifier.padding(end = 4.dp)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                eliminarUsuario(usuario.id)
                                                cargarUsuarios()
                                            }
                                        }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Insertar nuevo usuario", style = MaterialTheme.typography.h6)
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                insertarUsuario(nombre, email, password)
                                // Limpiar campos y recargar la lista
                                nombre = ""
                                email = ""
                                password = ""
                                cargarUsuarios()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Insertar Usuario")
                    }
                }
            }

            // Diálogo para editar usuario
            if (showEditDialog && usuarioAEditar != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Editar usuario") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = nuevoNombre,
                                onValueChange = { nuevoNombre = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = nuevoEmail,
                                onValueChange = { nuevoEmail = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = nuevoPassword,
                                onValueChange = { nuevoPassword = it },
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                scope.launch {
                                    usuarioAEditar?.let { usuario ->
                                        actualizarUsuario(
                                            usuario.id,
                                            nuevoNombre,
                                            nuevoEmail,
                                            nuevoPassword
                                        )
                                        cargarUsuarios()
                                        showEditDialog = false
                                    }
                                }
                            }
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

// Función para insertar usuario (como ya lo tienes)
suspend fun insertarUsuario(nombre: String, correo: String, contrasennia: String) {
    // Usamos signInAnonymously para fines de prueba (o puedes integrar Auth)
    supabaseClient.auth.signInAnonymously(data = null, captchaToken = null)
    val nuevoUsuario = Supausuario(
        nombre = nombre,
        email = correo,
        password = contrasennia
    )
    try {
        supabaseClient
            .from("supausuarios")
            .insert(nuevoUsuario)
            .decodeList<Supausuario>().run {
                println("Usuario insertado: $this")
            }
    } catch (e: Exception) {
        if (e.message?.contains("Expected start of the array") == true) {
            // Se ignora si la respuesta está vacía
        } else {
            throw e
        }
    }
}

suspend fun actualizarUsuario(id: Int, nuevoNombre: String, nuevoEmail: String, nuevoPassword: String) {
    val updateData = mapOf(
        "nombre" to nuevoNombre,
        "email" to nuevoEmail,
        "password" to nuevoPassword
    )
    try {
        supabaseClient
            .from("supausuarios")
            .update(updateData) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeList<Supausuario>()
            .run {
                println("Usuario actualizado: $this")
            }
    } catch (e: Exception) {
        throw Exception("Error actualizando usuario: ${e.message}")
    }
}

suspend fun eliminarUsuario(id: Int) {
    try {
        val response = supabaseClient
            .from("supausuarios")
            .delete {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeList<Supausuario>()

        if (response.isNotEmpty()) {
            println("Usuario eliminado: $response")
        } else {
            println("No se encontró ningún usuario con el ID proporcionado.")
        }
    } catch (e: Exception) {
        throw Exception("Error eliminando usuario: ${e.message}")
    }
}

@Serializable
data class Supausuario(
    val id: Int = 0,  // Se espera que sea autogenerado por Supabase
    val nombre: String,
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return "Usuario(id='$id', nombre='$nombre', email='$email', password='$password')"
    }
}