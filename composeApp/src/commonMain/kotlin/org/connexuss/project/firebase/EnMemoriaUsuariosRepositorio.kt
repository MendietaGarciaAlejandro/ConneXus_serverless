package org.connexuss.project.firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class EnMemoriaUsuariosRepositorio : UsuariosRepositorio {

    private val usuarios = MutableStateFlow<List<UsuarioPrueba>>(emptyList())
    private var id = 100

    init {
        usuarios.value = List(15) {
            UsuarioPrueba(
                idUsuarioPrueba = "ID: $it",
                nombre = "User $it",
                titulo = "Title $it",
                compannia = "Company $it"
            )
        }
    }

    override fun getUsuario(): Flow<List<UsuarioPrueba>> {
        return usuarios
    }

    override fun getUsuarioPorId(id: String): Flow<UsuarioPrueba?> {
        return usuarios.map { userList -> userList.find { it.idUsuarioPrueba == id } }
    }

    override suspend fun addUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value += usuarioPrueba.copy(idUsuarioPrueba = (id++).toString())
    }

    override suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value = usuarios.value.map {
            if (it.idUsuarioPrueba == usuarioPrueba.idUsuarioPrueba) usuarioPrueba else it
        }
    }

    override suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba) {
        usuarios.value = usuarios.value.filter { it.idUsuarioPrueba != usuarioPrueba.idUsuarioPrueba }
    }
}