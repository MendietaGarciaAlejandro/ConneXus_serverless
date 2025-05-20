package org.connexuss.project.supabase

import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.connexuss.project.misc.Supabase
import kotlin.random.Random

suspend fun subirImagenChat(nombreOriginal: String, bytes: ByteArray): String {
    val bucket = Supabase.client.storage.from("imageneschat")
    val nombreArchivo = "chat_${Random.nextInt(1000000)}_$nombreOriginal"

    return withContext(Dispatchers.Default) {
        bucket.upload(
            nombreArchivo, // path
            bytes          // file content
        ) {
            upsert = true  // this goes inside the builder
        }

        bucket.publicUrl(nombreArchivo)
    }
}
