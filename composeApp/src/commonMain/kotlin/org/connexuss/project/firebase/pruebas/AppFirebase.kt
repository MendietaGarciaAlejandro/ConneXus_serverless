package org.connexuss.project.firebase.pruebas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun AppFirebase(navHostController: NavHostController) {
    val userRepository = remember { FirestoreUserRepository() }
    UserScreen(userRepository)
}