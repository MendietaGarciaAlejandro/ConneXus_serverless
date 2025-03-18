package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow

interface UsuariosRepositorio {
    fun getUsuario(): Flow<List<UsuarioPrueba>>
    fun getUsuarioPorId(id: String): Flow<UsuarioPrueba?>
    suspend fun addUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba)
}