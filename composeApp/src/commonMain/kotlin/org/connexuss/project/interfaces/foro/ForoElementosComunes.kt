package org.connexuss.project.interfaces.foro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.whyoleg.cryptography.algorithms.AES
import org.connexuss.project.supabase.SupabaseRepositorioGenerico

// Repositorio genérico instanciado
val repoForo = SupabaseRepositorioGenerico()

// Clave simétrica sobreescibible para cada vez que se entra en un tema, se sobreescribe
object ClaveTemaHolder {
    var clave: AES.GCM.Key? by mutableStateOf(null)
}