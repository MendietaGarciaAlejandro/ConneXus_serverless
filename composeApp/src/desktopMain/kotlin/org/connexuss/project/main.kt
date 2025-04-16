package org.connexuss.project

//import android.app.Application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
//import com.google.firebase.FirebasePlatform
//import dev.gitlive.firebase.Firebase
//import dev.gitlive.firebase.FirebaseOptions
//import dev.gitlive.firebase.initialize

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
    ) {
        App()
    }
}