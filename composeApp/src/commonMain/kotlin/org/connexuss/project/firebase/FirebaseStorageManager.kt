package org.connexuss.project.firebase

object FirebaseStorageManager {
    suspend fun uploadFile(path: String, data: ByteArray): String {
        // Lógica para subir un archivo a Firebase Storage
        // Devuelve la URL del archivo
        return "url_del_archivo"
    }

    suspend fun downloadFile(path: String): ByteArray? {
        // Lógica para descargar un archivo de Firebase Storage
        return null
    }
}
