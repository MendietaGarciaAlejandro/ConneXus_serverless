package org.connexuss.project
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.externals.initializeApp
import org.jetbrains.compose.web.renderComposable

fun main() {

    // Configuración de Firebase
    val options = FirebaseOptions(
        applicationId = "1:401361889791:web:2e118f9c222e80f04fdfca",
        apiKey = "AIzaSyBryvu63OkbvUWPic59iJY-AtrFpACBjhk",
        authDomain = "connexus-dam.firebaseapp.com",
        projectId = "connexus-dam",
        storageBucket = "connexus-dam.firebasestorage.app",
        gcmSenderId = "401361889791"
        // measurementId no es obligatorio en este SDK
    )

    initializeApp(options)

    // Inicializa Firebase con las opciones definidas
    //Firebase.initialize(options)

    // Renderiza la app en el elemento con id "root"
    renderComposable(rootElementId = "root") {
        App()
    }
}