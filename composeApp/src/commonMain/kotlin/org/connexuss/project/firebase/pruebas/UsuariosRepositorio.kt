package org.connexuss.project.firebase.pruebas

import kotlinx.coroutines.flow.Flow
import org.connexuss.project.usuario.Usuario

interface UsuariosRepositorio {
    fun getUsuario(): Flow<List<UsuarioPrueba>>
    fun getUsuarioPorId(id: String): Flow<UsuarioPrueba?>
    suspend fun addUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun updateUsuario(usuarioPrueba: UsuarioPrueba)
    suspend fun deleteUsuario(usuarioPrueba: UsuarioPrueba)
}

interface UsuariosNuestrosRepositorio {
    fun getUsuario(): Flow<List<Usuario>>
    fun getUsuarioPorId(id: String): Flow<Usuario?>
    suspend fun addUsuario(usuario: Usuario)
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(usuario: Usuario)
}