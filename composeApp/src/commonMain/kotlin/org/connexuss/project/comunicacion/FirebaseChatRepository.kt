package org.connexuss.project.comunicacion

// import com.google.firebase.firestore.FirebaseFirestore

class FirebaseChatRepository : ChatRepository {
    /*
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun sendMessage(roomId: String, message: ChatMessage): Boolean {
        return try {
            // Guarda el mensaje en la colección "chatrooms/{roomId}/messages"
            firestore.collection("chatrooms")
                .document(roomId)
                .collection("messages")
                .document(message.id)
                .set(message)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getChatRoom(roomId: String): ChatRoom? {
        // Implementa la lógica para obtener la sala con sus mensajes, por ejemplo, haciendo un query a Firestore.
        TODO("Implementar consulta a Firestore para obtener la sala de chat")
    }

    // Puedes agregar una función para convertir un listener en un Flow:
    fun listenChatMessages(roomId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listenerRegistration = firestore.collection("chatrooms")
            .document(roomId)
            .collection("messages")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val mensajes = snapshot?.documents?.mapNotNull { it.toObject(ChatMessage::class.java) } ?: emptyList()
                trySend(mensajes)
            }
        awaitClose { listenerRegistration.remove() }
    }
    */
    override suspend fun sendMessage(roomId: String, message: Mensaje): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getChatRoom(roomId: String): Conversacion? {
        TODO("Not yet implemented")
    }
}