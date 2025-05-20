package org.connexuss.project.interfaces.foro.estado

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase para gestionar el estado de un hilo del foro.
 * Controla aspectos como el conteo de nuevos posts y refrescos.
 */
class HiloState(val hiloId: String) {
    val newPostsCount = mutableStateOf(0)
    private val _isActive = MutableStateFlow(true)

    fun reset() {
        newPostsCount.value = 0
    }

    suspend fun stop() {
        _isActive.value = false
    }
}
