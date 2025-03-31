package org.connexuss.project.supabase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho
import org.connexuss.project.interfaces.traducir

// Inicializa el cliente de Supabase con tus credenciales
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://vspadmmdpsvzbnbprijo.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZzcGFkbW1kcHN2emJuYnByaWpvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMyODY3MzgsImV4cCI6MjA1ODg2MjczOH0.7nrimK0WjGsnlpCI9pq4JumW7g-vysAk2kVb0ZhQ8Tc",
) {
    install(Storage)
    install(Auth)
    install(Realtime)
    install(Postgrest)
}

// Estado para la UI de usuarios
data class UsersState(
    val users: List<UsuarioSupabase> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun MostrarUsuariosSupabase(navController: NavHostController) {
    // Estado para la lista de usuarios
    var state by remember { mutableStateOf(UsersState()) }
    val scope = rememberCoroutineScope()

    // Campos para insertar un nuevo usuario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Función para refrescar la lista de usuarios
    fun refreshUsers() {
        scope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val usuarios = supabaseClient
                    .from("suprausuarios")
                    .select()
                    .decodeList<UsuarioSupabase>()
                state = state.copy(users = usuarios, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(error = "Error: ${e.message}", isLoading = false)
            }
        }
    }

    // Carga inicial de usuarios
    LaunchedEffect(Unit) {
        refreshUsers()
    }
    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = traducir("iniciar_sesion"),
                    navController = navController,
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
                    // Interfaz
                    Column(modifier = modifier.fillMaxSize().padding(padding)) {
                        Text("Usuarios registrados", style = MaterialTheme.typography.h6)
                        when {
                            state.isLoading -> Text("Cargando...")
                            state.error != null -> Text(
                                state.error!!,
                                color = MaterialTheme.colors.error
                            )

                            else -> LazyColumn(modifier = Modifier.weight(1f)) {
                                items(state.users) { usuario ->
                                    // Se muestra el nombre de cada usuario
                                    Text(
                                        text = usuario.nombre,
                                        style = MaterialTheme.typography.body1
                                    )
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
                                    try {
                                        insertarUsuario(nombre, email, password)
                                        // Limpiar campos después de insertar
                                        nombre = ""
                                        email = ""
                                        password = ""
                                        refreshUsers()
                                    } catch (e: Exception) {
                                        state =
                                            state.copy(error = "Error insertando usuario: ${e.message}")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Insertar Usuario")
                        }
                    }
                }
            }
        }
    }
}

// Función para insertar usuario usando Supabase Auth y luego datos en la tabla
suspend fun insertarUsuario(nombre: String, correo: String, contrasennia: String) {
    // Registrar usuario en Auth
    val authResponse = supabaseClient.auth.signUpWith(Email) {
        this.email = correo
        password = contrasennia
    }
    // Insertar datos adicionales en la tabla 'suprausuarios'
    val nuevoUsuario = UsuarioSupabase(
        nombre = nombre,
        email = correo,
        password = contrasennia
    )
    // decodeSingle lanzará una excepción si ocurre un error
    val insertedUser = supabaseClient
        .from("suprausuarios")
        .insert(nuevoUsuario)
        .decodeSingle<UsuarioSupabase>()
    // Si llegamos acá, la inserción fue exitosa y 'insertedUser' contiene el usuario insertado.
}


@Serializable
data class UsuarioSupabase(
    val id: Int = 0,  // Se espera que sea autogenerado por Supabase
    val nombre: String,
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return "Usuario(id='$id', nombre='$nombre', email='$email', password='$password')"
    }
}