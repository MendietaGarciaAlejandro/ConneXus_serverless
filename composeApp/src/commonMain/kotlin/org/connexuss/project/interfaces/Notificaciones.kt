package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectAsFlow
import org.connexuss.project.misc.Supabase
import org.connexuss.project.comunicacion.Post
import androidx.compose.material.*


class HiloViewModel : ViewModel() {
    private var _newPostsCount by mutableStateOf(0)
    val newPostsCount: State<Int> get() = mutableStateOf(_newPostsCount)

    fun onNewPost() {
        _newPostsCount++
    }
    fun resetCounter() {
        _newPostsCount = 0
    }
}

val canal = Supabase.client.postgrest
    .from("post")
    .selectAsFlow(
        filter = PostgrestFilter.eq("hiloId", currentHiloId),
        primaryKey = Post::id
    )
    .collect { currentList ->
        val countNew = currentList.size - initialCount
        _newPostsCount.value = maxOf(0, countNew)
    }

@Composable
fun HiloTopBar(
    title: String,
    navController: NavHostController?,
    viewModel: HiloViewModel,
    showRefresh: Boolean = true,
    startRoute: String
) {
    TopAppBar(
        title = { Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        navigationIcon = { /* botón atrás si aplica */ },
        actions = {
            if (viewModel.newPostsCount.value > 0) {
                BadgeBox(
                    badgeContent = { Text("${viewModel.newPostsCount.value}") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Nuevos posts")
                }
            }
            if (showRefresh) {
                IconButton(onClick = {
                    viewModel.resetCounter()
                    navController?.currentDestination?.route?.let { current ->
                        navController.navigate(current) {
                            popUpTo(startRoute) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }
        }
    )
}
