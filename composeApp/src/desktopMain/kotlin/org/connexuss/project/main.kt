package org.connexuss.project

//import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.connexus
import connexus_serverless.composeapp.generated.resources.connexus_windows
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource

//import com.google.firebase.FirebasePlatform
//import dev.gitlive.firebase.Firebase
//import dev.gitlive.firebase.FirebaseOptions
//import dev.gitlive.firebase.initialize

@OptIn(InternalResourceApi::class)
fun main() = application {

//    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
//
//        val storage = mutableMapOf<String, String>()
//        override fun clear(key: String) {
//            storage.remove(key)
//        }
//
//        override fun log(msg: String) = println(msg)
//
//        override fun retrieve(key: String) = storage[key]
//
//        override fun store(key: String, value: String) = storage.set(key, value)
//
//    })
//
//    val options = FirebaseOptions(
//        projectId = "connexus-dam",
//        applicationId = "1:401361889791:web:2e118f9c222e80f04fdfca",
//        apiKey = "AIzaSyBryvu63OkbvUWPic59iJY-AtrFpACBjhk"
//    )

    /*
    // Firebase configuracion (Cuenta alexmengar)
    const firebaseConfig = {
        apiKey: "AIzaSyBUhPRsx7QaYJJ62Cq7zHsIIkroqe375jM",
        authDomain: "connexus-serverless.firebaseapp.com",
        projectId: "connexus-serverless",
        storageBucket: "connexus-serverless.firebasestorage.app",
        messagingSenderId: "1060908289340",
        appId: "1:1060908289340:web:f2f45f66720d0114c02239",
        measurementId: "G-QQTKPDXZP5"
    };
     */

    // Firebase configuracion (Cuenta ConneXus)
    /*
    const firebaseConfig = {
        apiKey: "AIzaSyBryvu63OkbvUWPic59iJY-AtrFpACBjhk",
        authDomain: "connexus-dam.firebaseapp.com",
        projectId: "connexus-dam",
        storageBucket: "connexus-dam.firebasestorage.app",
        messagingSenderId: "401361889791",
        appId: "1:401361889791:web:2e118f9c222e80f04fdfca",
        measurementId: "G-7L6GHVH04W"
    };
     */

    //Firebase.initialize(Application(), options)

    Window(
        onCloseRequest = ::exitApplication,
        title = "ConneXus_serverless",
        icon = painterResource(Res.drawable.connexus),
    ) {
        // Contenedor de toda la ventana para capturar Escape
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Escape) {
                        exitApplication()
                        true
                    } else {
                        false
                    }
                }
        ) {
            App()
        }
    }
}