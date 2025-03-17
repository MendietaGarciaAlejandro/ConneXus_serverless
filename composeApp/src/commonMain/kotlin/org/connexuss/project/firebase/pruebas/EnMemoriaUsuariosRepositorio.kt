package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class EnMemoriaUsuariosRepositorio : UsuariosRepositorio {

    private val usuarios = MutableStateFlow<List<UsuarioPrueba>>(emptyList())
    private var id = 100

    init {
        usuarios.value = List(15) {
            UsuarioPrueba(
                id = "ID: $it",
                name = "User $it",
                title = "Title $it",
                company = "Company $it"
            )
        }
    }

    override fun getUsuario(): Flow<List<UsuarioPrueba>> {
        return usuarios
    }

    override fun getUsuarioPorId(id: String): Flow<UsuarioPrueba?> {
        return usuarios.map { userList -> userList.find { it.id == id } }
    }

    override suspend fun addUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value += usuarioPrueba.copy(id = (id++).toString())
    }

    override suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value = usuarios.value.map {
            if (it.id == usuarioPrueba.id) usuarioPrueba else it
        }
    }

    override suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value = usuarios.value.filter { it.id != usuarioPrueba.id }
    }
}